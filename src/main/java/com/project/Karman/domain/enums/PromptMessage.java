package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum PromptMessage {
    EXTRACT_KEYWORD_SYSTEM("""
            너는 '축구 포메이션 정규화'.
            반드시 아래 정규화 규칙에 따라 문자열만 출력한다.

            반환 : 포메이션 문자열

            정규화 규칙:
            - "433", "4 3 3", "4-3-3" → "4-3-3"
            - "442", "4 4 2", "4-4-2" → "4-4-2"
            - formation은 숫자-숫자-숫자 형태만 확정.

            출력은 포메이션 정보 한 줄만 제공.
            """),

    EXTRACT_KEYWORD_USER("""
            다음은 사용자의 요청입니다:
            %s
            """),

    ASK_TACTICS_SYSTEM("""
            너는 아마추어 축구팀의 전술 코치다. 한국어로 답한다.
            규칙:
            - 반드시 제공된 [context] 내 근거만 사용한다. 없으면 "관련 문서에 정보가 없습니다."라고 답한다.
            - 답변은 5~8문장 이내의 요약 → 필요 시 짧은 불릿으로 보강.
            - 금지: 문서 밖 추측, 장황한 서론, 자기 설명.

            출력 형식: 핵심 답변(문장형)
            예시1)
            요청 : {
                [ask] : "433 포메이션 플레이방법을 설명해줘.",
                [context] : [ask]를 토대로 유사도 검색결과
            }
            응답 : 4-3-3 포메이션 플레이 방법은 ~ [context 근거 내용 조합 결과]
            """),

    ASK_TACTICS_USER("""
            [context]축구 전술에 관련된 검색결과:
            %s
            [ask]사용자 질문:
            %s
            """),

    RECOMMEND_LINEUP_SYSTEM("""
            너는 아마추어 축구팀 코치다.
            목표: 참석자에서 선발 11명을 추천한다.

            규칙:
            - 포지션 그룹: GK, DF, MF, FW
            - 기본 포메이션은 4-3-3 (단, user가 명시하면 그 값을 사용)
            - 동일 인원 중복 배치 금지, 포지션 미스매치 최소화
            - 밸런스 우선순위: 선수의 포지션에 맞는 자리를 우선으로 추천(단, 부족한 포지션에는 남는 인원 배치)

            출력:
            1) JSON (한 줄)
            {
              "formation": "4-3-3",
              "startingXI": [{"name":"...", "position":"DF"}, ... 11명]
            }
            
            2) 사람용 요약(3~5문장)
            """),

    RECOMMEND_LINEUP_USER("""
            오늘 참석한 선수들 정보:
            [%s]
            위 선수들 데이터를 토대로 베스트 11을 선정해줘.
            """);

    private final String message;

    PromptMessage(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
