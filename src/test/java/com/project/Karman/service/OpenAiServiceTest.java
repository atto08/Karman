package com.project.Karman.service;

import com.project.Karman.domain.enums.PromptMessage;
import org.springframework.ai.chat.messages.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class OpenAiServiceTest {

    @Mock
    OpenAiChatModel chatModel;

    OpenAiService openAiService;

    @BeforeEach
    void setUp() {
        // createPrompt가 외부 의존 안 쓰면 이렇게만 초기화해도 OK
        openAiService = new OpenAiService(chatModel);
    }

    @Test
    @DisplayName("전술 질문 - 컨텍스트 있는 프롬프트 생성")
    void createPromptWithContext() {
        // given
        String ask = "433 포메이션에서 미드필더의 역할을 설명해줘.";
        String context = """
                [individual-role]
                [4-3-3 포메이션 포지션 별 책임] #MF
                (생략) ...
                """;

        // 기대값은 enum의 format을 그대로 사용
        String expectedSystem = PromptMessage.ASK_TACTICS_SYSTEM.getMessage();
        String expectedUser = PromptMessage.ASK_TACTICS_USER.format(context, ask);

        // when
        Prompt prompt = openAiService.createPrompt(
                PromptMessage.ASK_TACTICS_SYSTEM,
                PromptMessage.ASK_TACTICS_USER,
                ask,
                context);

        // then
        List<Message> msgs = prompt.getInstructions(); // 보통 List<Message>
        assertThat(msgs).hasSize(2);

        assertThat(msgs.get(0)).isInstanceOf(org.springframework.ai.chat.messages.SystemMessage.class);
        assertThat((msgs.get(0)).getText()).isEqualTo(expectedSystem);

        assertThat(msgs.get(1)).isInstanceOf(org.springframework.ai.chat.messages.UserMessage.class);
        assertThat((msgs.get(1)).getText()).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("라인업 추천 - 컨텍스트 없는 프롬프트 생성")
    void createPromptWithoutContext() {
        // given
        String ask = "433 포메이션에서 미드필더의 역할을 설명해줘.";

        String expectedSystem = PromptMessage.RECOMMEND_LINEUP_SYSTEM.getMessage();
        String expectedUser = PromptMessage.RECOMMEND_LINEUP_USER.format(ask);

        // when
        Prompt prompt = openAiService.createPrompt(
                PromptMessage.RECOMMEND_LINEUP_SYSTEM,
                PromptMessage.RECOMMEND_LINEUP_USER,
                ask,
                null);

        // then
        List<Message> msgs = prompt.getInstructions();
        assertThat(msgs).hasSize(2);

        assertThat((msgs.get(0)).getText()).isEqualTo(expectedSystem);
        assertThat((msgs.get(1)).getText()).isEqualTo(expectedUser);
    }
}