package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.enums.PromptMessage;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineupService {
    private final OpenAiService openAiService;
    private final ClubRepository clubRepository;
    private final AffiliationRepository affiliationRepository;

    @Transactional(readOnly = true)
    public String recommendLineup(UUID clubId, List<UUID> attendPlayers) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 소속 선수 정보 조회
        List<Affiliation> playerInfoList = affiliationRepository.findAllByClub_ClubIdAndAffiliationIds(clubId, attendPlayers);
        // 소속 선수 정보 파싱
        String playerRecords = playerInfoList.stream()
                .map(player -> String.format("{ name: \"%s\", position: \"%s\", back_number: \"%s\"}",
                        player.getPlayerName(), player.getPlayerPosition(), player.getBackNumber()))
                .collect(Collectors.joining(",\n"));
        // 프롬프트 생성 - 사용자 요청
        Prompt prompt = openAiService.createPrompt(PromptMessage.RECOMMEND_LINEUP_SYSTEM, PromptMessage.RECOMMEND_LINEUP_USER, playerRecords, null);
        // Ai 응답 생성 - 사용자 요청
        return openAiService.askChatModel(prompt).getResult().getOutput().getText();
    }
}
