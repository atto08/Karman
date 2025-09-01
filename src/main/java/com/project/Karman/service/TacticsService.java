package com.project.Karman.service;

import com.project.Karman.domain.enums.PromptMessage;
import com.project.Karman.repository.TacticalDocumentRepository;
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
    private final TacticalDocumentRepository tacticalDocumentRepository;

    @Transactional
    public void indexTactics(String request) {
        // 파싱해서 내용을 List<Document> 화 시키기.
        documentService.indexDocument(request);
    }

    @Transactional
    public String askTacticalCoach(String ask) {

        // TODO: 추출 키워드 토대로 유사도 검색하는 방향으로 개선
//        // 1) 키워드 추출
//        // 프롬프트 생성 - 키워드 추출
//        Prompt keywordPrompt = openAiService.createPrompt(PromptMessage.EXTRACT_KEYWORD, ask, null);
//        // Ai 응답 생성 - 키워드 추출
//        String extractKeywords = openAiService.askChatModel(keywordPrompt).getResult().getOutput().getText();
//        log.info("--extract keywords--{}", extractKeywords);
//
//        // 2) 포메이션 키워드 일치하는 리스트
//        List<Map<String, Object>> result = tacticalDocumentRepository.findByMetadataJson(extractKeywords);
//        log.info("result size : {}", result.size());
//        for (Map<String, Object> info : result) {
//            log.info("result info : {}", info);
//        }

        // 2) 유사도 검색 결과와 키워드 비교
        // 유사도 검색 결과 리스트 - 부정확
        List<Document> documents = documentService.similaritySearch(ask);
        log.info("Document Count:{}", documents.size());

        // 검색 결과 통합
        StringBuilder context = new StringBuilder();
        log.info("Start-- print Document Text");
        for (Document document : documents) {
            log.info(document.getText());
            context.append(document.getText()).append("\n");
        }
        log.info("Finish-- print Document Text");

        // 프롬프트 생성 - 사용자 요청
        Prompt prompt = openAiService.createPrompt(PromptMessage.RESPONSE_USER_ASK, ask, context.toString());
        // Ai 응답 생성 - 사용자 요청
        return openAiService.askChatModel(prompt).getResult().getOutput().getText();
    }
}
