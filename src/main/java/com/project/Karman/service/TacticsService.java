package com.project.Karman.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Karman.domain.PromptMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TacticsService {

    private final DocumentService documentService;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void indexTactics(String request) {
        // 파싱해서 내용을 List<Document> 화 시키기.
        documentService.indexDocument(request);
    }

    @Transactional
    public String askTacticalCoach(String ask) {

        // 1) 키워드 추출
        // 프롬프트 생성 - 키워드 추출
        Prompt keywordPrompt = openAiService.createPrompt(PromptMessage.EXTRACT_KEYWORD, ask, null);
        // Ai 응답 생성 - 키워드 추출
        String extractKeywords = openAiService.askChatModel(keywordPrompt).getResult().getOutput().getText();
        log.info("--extract keywords--{}", extractKeywords);

        // 추출 키워드 to String LIST
        List<String> keywords = jsonToList(extractKeywords);
        log.info("List keywords--{}", keywords);

        // 2) 유사도 검색 결과와 키워드 비교
        // 유사도 검색 결과 리스트 - 부정확
        List<Document> documents = documentService.similaritySearch(ask);
        log.info("Document Count:{}", documents.size());

        // 유사도 검색 결과에서 키워드가 전부 존재하는 경우만 추출
        List<Document> filteredDocs = documents.stream()
                .filter(doc -> keywords.stream().allMatch(keyword ->
                        doc.getText().contains(keyword)))
                .toList();
        log.info("Filtered Docs:{}", filteredDocs);

        // 3) 사용자 요청 응답 생성 및 반환
        // 검색 결과 통합
        StringBuilder context = new StringBuilder();
        log.info("Start-- print Document Text");
        for (Document document : filteredDocs) {
            log.info(document.getText());
            context.append(document.getText()).append("\n");
        }
        log.info("Finish-- print Document Text");

        // 프롬프트 생성 - 사용자 요청
        Prompt prompt = openAiService.createPrompt(PromptMessage.RESPONSE_USER_ASK, ask, context.toString());
        // Ai 응답 생성 - 사용자 요청
        return openAiService.askChatModel(prompt).getResult().getOutput().getText();
    }

    private List<String> jsonToList(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode keywordsNode = root.get("keywords");
            if (keywordsNode == null || !keywordsNode.isArray()) {
                return Collections.emptyList();
            }

            List<String> keywords = new ArrayList<>();
            for (JsonNode keyword : keywordsNode) {
                keywords.add(keyword.asText());
            }
            return keywords;

        } catch (Exception e) {
            throw new IllegalArgumentException("JSON 키워드 파싱 실패: " + json, e);
        }
    }
}
