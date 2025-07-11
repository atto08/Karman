package com.project.Karman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiChatModel chatModel;

    @PostMapping("/ai/generate")
    public Map<String, String> generate(@RequestBody String message) {
        return Map.of("generation", chatModel.call(message));
    }
}
