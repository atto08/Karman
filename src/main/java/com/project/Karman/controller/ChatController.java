package com.project.Karman.controller;

import com.project.Karman.dto.request.AttendPlayerRequest;
import com.project.Karman.service.LineupService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/ai")
public class ChatController {

    private final OpenAiChatModel chatModel;
    private final LineupService lineupService;

    @PostMapping
    public Map<String, String> generate(@RequestBody String message) {
        return Map.of("generation", chatModel.call(message));
    }


    @PostMapping("/recommend/lineup")
    public Map<String, String> recommendLineup(@RequestBody AttendPlayerRequest request) {
        String message = lineupService.recommendLineup(request.attendPlayers(), request.clubId());
        return Map.of("ai-response", message);
    }
}
