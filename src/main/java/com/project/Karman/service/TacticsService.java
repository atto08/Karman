package com.project.Karman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TacticsService {

    private final DocumentService documentService;
    private final OpenAiService openAiService;

    @Transactional
    public void indexTactics(String request) {
        // 파싱해서 내용을 List<Document> 화 시키기.
        documentService.indexDocument(request);
    }

    @Transactional
    public String askTacticalCoach(String ask) {

        // 유사도 검색 - 결과 리스트
        List<Document> documents = documentService.similaritySearch(ask);

        // 검색 결과 통합
        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            context.append(document.getText()).append("\n");
        }

        // 프롬프트 생성
        Prompt prompt = openAiService.createPrompt(ask, context.toString());

        return openAiService.askAiCoach(prompt).getResult().getOutput().getText();
    }
}
