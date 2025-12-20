package com.project.Karman.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.PromptMessage;
import com.project.Karman.dto.response.LineupPlayerInfo;
import com.project.Karman.dto.response.LineupRecommendResponseDto;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineupService {
    private final OpenAiService openAiService;
    private final ClubRepository clubRepository;
    private final AffiliationRepository affiliationRepository;

    @Transactional(readOnly = true)
    public LineupRecommendResponseDto recommendLineup(Member member, UUID clubId, List<UUID> attendPlayers) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 로그인 유저 클럽 소속 여부 체크
        if (!isMemberOfClub(clubId, member.getMemberId())) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_USER_ACCESS_MATCH_DATA);
        }
        // 소속 선수 정보 조회
        List<Affiliation> playerInfoList = affiliationRepository.findAllByClub_ClubIdAndAffiliationIdIn(clubId, attendPlayers);
        // 소속 선수 정보 파싱
        String playerRecords = playerInfoList.stream()
                .map(player -> String.format("{ affiliationId: \"%s\", name: \"%s\", position: \"%s\", back_number: \"%s\"}",
                        player.getAffiliationId(),
                        player.getPlayerName(),
                        player.getPlayerPosition(),
                        player.getBackNumber()))
                .collect(Collectors.joining(",\n"));
        // 프롬프트 생성 - 사용자 요청
        Prompt prompt = openAiService.createPrompt(
                PromptMessage.RECOMMEND_LINEUP_SYSTEM,
                PromptMessage.RECOMMEND_LINEUP_USER,
                playerRecords,
                null);
        // Ai 응답 생성 - 사용자 요청
        String chatModelResponse = openAiService.askChatModel(prompt).getResult().getOutput().getText();

        try {
            // JSON 형식으로 변환
            ObjectMapper jsonMapper = new ObjectMapper();
            // DTO 매핑
            LineupRecommendResponseDto aiResponse = jsonMapper.readValue(chatModelResponse, LineupRecommendResponseDto.class);

            // AI 응답 검증
            validateAiLineupResponse(aiResponse, attendPlayers);

            return aiResponse;
        } catch (JsonProcessingException e) {
            throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
        }
    }

    private Boolean isMemberOfClub(UUID clubId, UUID memberId) {
        return affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatus(
                clubId,
                memberId,
                ClubJoinStatus.APPROVED);
    }

    /**
     * AI 응답의 라인업 추천 결과를 검증합니다.
     * - affiliationId가 유효한 UUID인지 확인
     * - affiliationId가 참석 선수 목록에 포함되어 있는지 확인
     * - 중복된 선수가 선택되지 않았는지 확인
     */
    private void validateAiLineupResponse(LineupRecommendResponseDto response, List<UUID> attendPlayers) {
        if (response == null || response.startingXI() == null) {
            throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
        }

        List<LineupPlayerInfo> startingXI = response.startingXI();

        // 선발 인원이 11명인지 확인
        if (startingXI.size() != 11) {
            throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
        }

        // 참석 선수 목록을 Set으로 변환하여 빠른 조회 가능하도록 함
        Set<UUID> attendPlayerSet = new HashSet<>(attendPlayers);

        // 중복 확인을 위한 Set
        Set<UUID> usedAffiliationIds = new HashSet<>();

        for (LineupPlayerInfo player : startingXI) {
            UUID affiliationId = player.affiliationId();

            // 1. affiliationId가 null이 아닌지 확인
            if (affiliationId == null) {
                throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
            }

            // 2. 중복된 선수가 선택되지 않았는지 확인
            if (usedAffiliationIds.contains(affiliationId)) {
                throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
            }
            usedAffiliationIds.add(affiliationId);

            // 3. affiliationId가 참석 선수 목록에 포함되어 있는지 확인
            if (!attendPlayerSet.contains(affiliationId)) {
                throw new CustomException(ExceptionMessage.CREATE_AI_RESPONSE_ERROR);
            }
        }
    }
}
