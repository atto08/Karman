package com.project.Karman.service;

import com.project.Karman.domain.PromptMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final OpenAiChatModel chatModel;

    public OpenAiService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public Prompt createPrompt(PromptMessage promptMessage, String ask, @Nullable String context) {
        // 요청 메시지 생성
        String request = (context == null) ? promptMessage.format(ask) : promptMessage.format(context, ask);
        // 증강 + 프롬프트 생성
        return new Prompt(new UserMessage(request));
    }

    public ChatResponse askChatModel(Prompt prompt) {
        return chatModel.call(prompt);
    }
}
