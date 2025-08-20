package com.project.Karman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TacticsService {

    private final VectorStore vectorStore;
    private final OpenAiChatModel chatModel;
    private final DocumentService documentService;

    @Transactional
    public void indexTactics(String request) {

        // 파싱해서 내용을 List<Document> 화 시키기.
        documentService.indexDocument(request);
    }

    @Transactional
    public String askTacticalCoach(String ask) {

        // 유사도 검색 - 결과 리스트
        List<Document> documents = vectorStore.similaritySearch(ask);

        // 검색 결과 통합
        StringBuilder context = new StringBuilder();
        for (Document doc : documents) {
            context.append(doc.getText()).append("\n");
        }

        // 요청 메시지 생성
        String request = String.format("""
                다음은 참고 문서입니다:
                %s
                            
                위 문서를 바탕으로 다음 질문에 답해주세요:
                %s
                """, context, ask);

        // 증강 + 프롬프트 생성
        Prompt prompt = new Prompt(new UserMessage(request));

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}
