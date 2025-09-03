package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum PromptMessage {
    EXTRACT_KEYWORD_USER("""
            다음은 사용자의 요청입니다:
            %s \n
            위 질문에서 포메이션을 정보를 추출해서 문자열로 반환해줘. \n
            """),

    EXTRACT_KEYWORD_SYSTEM("""
            포메이션 정보를 문자열로 추출하는게 너의 역할.
            - 포메이션 정보에는 '-'를 숫자 사이에 붙여서 "4-3-3" 같은 형태로 만들어줘.
            
            아래는 너의 이해를 돕는 예시야.
            예시1)
            입력: "433에서 미드필더의 역할에 대해 설명해줘."
            반환: 4-3-3
            예시2)
            입력: "442 포메이션에서 공격수와 수비수는 어떻게 움직여야해?"
            반환: 4-4-2
            """),

    ASK_TACTICS_USER("""
            다음은 참고 문서입니다:
            %s \n
            위 문서를 바탕으로 아래 사용자 질문에 답해주세요:
            %s \n
            """),

    ASK_TACTICS_SYSTEM("""
            너는 아마추어 축구팀의 전술 코치야.
            유저가 물어보는 전술, 포메이션에 관련된 내용을 설명해줘.
            """),

    RECOMMEND_LINEUP_USER("""
            오늘 참석한 선수 리스트:
            [%s]

            오늘 참석한 인원 리스트를 토대로 선발 선수 11명을 구성해줘.
            """),

    RECOMMEND_LINEUP_SYSTEM("""
            너는 아마추어 축구팀의 코치야.
            오늘 참석한 선수 목록에서 선발 라인업을 추천해줘.
            포지션은 크게 GK, DF, MF, FW로 구분되고, 각 선수의 포지션과 밸런스를 고려해줘.
            """),;

    private final String message;

    PromptMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
