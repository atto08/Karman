package com.project.Karman.domain.enums;

public enum PromptMessage {

    RECOMMEND_LINEUP_SYSTEM("""
                너는 아마추어 풋살팀의 전술 코치야. 오늘 참석한 선수 리스트에서 선발 라인업을 추천해줘.
                경기 포지션은 골키퍼, 수비수, 미드필더, 공격수로 나뉘고, 포지션 밸런스를 고려해줘.
            """),

    RECOMMEND_LINEUP_USER("""
                선수 정보 목록:
                [%s]

                선발 5명, 후보 3명으로 구성해줘.
            """);

    private final String message;

    PromptMessage(String message) {
        this.message = message;
    }

    public String value() {
        return message;
    }
}
