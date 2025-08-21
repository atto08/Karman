package com.project.Karman.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final OpenAiChatModel chatModel;

    public OpenAiService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public Prompt createPrompt(String ask, String context) {
        // 요청 메시지 생성
        String request = String.format("""
                다음은 참고 문서입니다:
                %s
                            
                위 문서를 바탕으로 다음 질문에 답해주세요:
                %s
                """, context, ask);
        // 증강 + 프롬프트 생성
        return new Prompt(new UserMessage(request));
    }

    public ChatResponse askAiCoach(Prompt prompt) {
        return chatModel.call(prompt);
    }
}
