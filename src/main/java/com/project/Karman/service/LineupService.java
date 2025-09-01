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
    public String recommendLineup(List<UUID> attendPlayers, UUID clubId) {

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클럽입니다."));

        List<Affiliation> playerInfoList = affiliationRepository.findAllByClubIdAndMemberIds(club.getClubId(), attendPlayers);

        String playerRecords = playerInfoList.stream()
                .map(player -> String.format("{ name: \"%s\", position: \"%s\", back_number: \"%s\"}",
                        player.getMember().getName(), player.getPosition(), player.getBackNumber()))
                .collect(Collectors.joining(",\n"));

        Prompt prompt = new Prompt(List.of(
                new SystemMessage(PromptMessage.RECOMMEND_LINEUP_SYSTEM.getMessage()),
                new UserMessage(PromptMessage.RECOMMEND_LINEUP_USER.getMessage().formatted(playerRecords))
        ));

        return openAiService.askChatModel(prompt).getResult().getOutput().getText();
    }
}
