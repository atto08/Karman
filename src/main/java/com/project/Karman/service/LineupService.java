package com.project.Karman.service;


import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.enums.PromptMessage;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineupService {

    private final OpenAiChatModel chatModel;
    private final ClubRepository clubRepository;
    private final AffiliationRepository affiliationRepository;

    @Transactional(readOnly = true)
    public String recommendLineup(List<UUID> attendPlayers, UUID clubId) {
        // club, affiliation 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클럽입니다."));

        // 우리 팀 참석선수 정보 목록
        List<Affiliation> playerInfoList = affiliationRepository.findAllByClubIdAndMemberIds(clubId, attendPlayers);

        // 선수 정보 문자열 구성
        String records = playerInfoList.stream()
                .map(a -> String.format("{ name: \"%s\", position: \"%s\", back_number: \"%s\"}",
                        a.getMember().getName(), a.getPosition(), a.getBackNumber()))
                .collect(Collectors.joining(",\n"));

        // 프롬프트 작성
        Prompt prompt = new Prompt(List.of(
                new SystemMessage(PromptMessage.RECOMMEND_LINEUP_SYSTEM.value()),
                new UserMessage(PromptMessage.RECOMMEND_LINEUP_USER.value().formatted(records))
        ));

        // 실제 응답 텍스트 추출
        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
