package com.project.Karman.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DocumentService {

    private final VectorStore vectorStore;

    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Transactional
    public void indexDocument(String request) {
        List<Document> documents = new ArrayList<>();
        // 청크별로 문단 나누기
        String[] chunks = request.split("\\\\\n");
        // 첫 번째는 카테고리와 포메이션명/전술명
        String[] categoryAndTitle = chunks[0].split("&");
        // 카테고리와 포메이션명 or 전술명
        String category = categoryAndTitle[0], title = categoryAndTitle[1];
        // 포메이션 전술과 관련된 리스트 벡터화
        for (int i = 1; i < chunks.length; i++) {
            HashMap<String, Object> map = divideMetaData(chunks[i], category, title);
            Document document = new Document(chunks[i], map);
            documents.add(document);
        }
        // 벡터 데이터 저장
        vectorStore.add(documents);
    }

    private HashMap<String, Object> divideMetaData(String context, String category, String title) {
        HashMap<String, Object> map = new HashMap<>();
        if (category.equals("formation")) {
            map.put("category", category);
            map.put("formation", title);
        } else {
            map.put("tactics", category);
            map.put("strategy", title);
        }

        String[] contents = context.split("\\n");
        String subtopic = contents[0].substring(1, contents[0].length() - 1);
        map.put("subtopic", subtopic);

        if (!subtopic.equals("intro")) {
            String div = contents[1].split("#")[1];

            if (subtopic.equals("individual-role")) {
                map.put("position", div);
            } else {
                int num = div.charAt(0) - 48;
                map.put("no", num);

                if (subtopic.equals("how-to-play")) {
                    String style = (contents[1].contains("공격")) ? "attack" : "defend";
                    map.put("style", style);
                }
            }
        }

        return map;
    }

    @Transactional(readOnly = true)
    public List<Document> similaritySearch(String ask) {
        return vectorStore.similaritySearch(ask);
    }
}
