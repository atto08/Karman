package com.project.Karman.service;

import com.project.Karman.domain.enums.PromptMessage;
import com.project.Karman.dto.request.AskTacticsRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TacticsService {

    private final DocumentService documentService;
    private final OpenAiService openAiService;

    @Transactional
    public void indexTactics(String request) {
        // 파싱해서 내용을 List<Document> 화 시키기.
        documentService.indexDocument(request);
    }

    @Transactional
    public String askTacticalCoach(AskTacticsRequestDto request) {

        // 1) 키워드 추출
        // 프롬프트 생성 - 키워드 추출
        Prompt keywordPrompt = openAiService.createPrompt(PromptMessage.EXTRACT_KEYWORD_SYSTEM, PromptMessage.EXTRACT_KEYWORD_USER, request.ask(), null);
        // Ai 응답 생성 - 키워드 추출
        String formation = openAiService.askChatModel(keywordPrompt).getResult().getOutput().getText();
        log.info("---formation: {} & subtopic: {}", formation, request.subtopic());

        // 2) 유사도 검색 - metadata로 범위 축소
        List<Document> documents = documentService.similaritySearch(request.ask(), request.subtopic(), formation);
        log.info("---유사도 검색 결과: {}", documents.size());
        // 유사도 검색 결과 통합 - context
        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            log.info("---문서정보: {}",document.getText());
            context.append(document.getText()).append("\n");
        }

        // 프롬프트 생성 - 사용자 요청
        Prompt prompt = openAiService.createPrompt(PromptMessage.ASK_TACTICS_SYSTEM, PromptMessage.ASK_TACTICS_USER, request.ask(), context.toString());
        // Ai 응답 생성 - 사용자 요청
        return openAiService.askChatModel(prompt).getResult().getOutput().getText();
    }
}
