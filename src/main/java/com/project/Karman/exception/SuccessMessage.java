package com.project.Karman.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessMessage {
    // 회원가입, 로그인 성공
    SIGNUP(HttpStatus.OK, "회원가입 완료", 200),
    LOGIN(HttpStatus.OK, "로그인 성공", 200),

    // 클럽 서비스
    CREATE_CLUB(HttpStatus.OK, "클럽 생성 완료", 200),
    GET_CLUB_INFO(HttpStatus.OK, "클럽 상세 정보 조회", 200),
    UPDATE_CLUB(HttpStatus.OK, "클럽 정보 수정 완료", 200),
    DELETE_CLUB(HttpStatus.OK, "클럽 삭제 완료", 200),
    SEARCH_CLUB(HttpStatus.OK, "클럽 검색 완료", 200),
    REQUEST_JOIN_CLUB(HttpStatus.OK, "클럽 가입 신청 완료", 200),
    ACCEPT_JOIN_CLUB_REQUEST(HttpStatus.OK, "클럽 가입 요청 승인", 200),
    REJECT_JOIN_CLUB_REQUEST(HttpStatus.OK, "클럽 가입 요청 거부", 200),
    WITHDRAW_CLUB(HttpStatus.OK, "클럽 탈퇴 완료.", 200),
    GET_PLAYERS_IN_CLUB(HttpStatus.OK, "클럽 소속 선수들 정보 조회", 200),
    UPDATE_PLAYER_INFO(HttpStatus.OK, "선수 정보 수정 완료", 200),

    // 매치 서비스
    CRATE_MATCH(HttpStatus.OK, "신규 매치등록 완료", 200),
    GET_MATCH_ALL(HttpStatus.OK, "매치 전체 기록 조회", 200),
    CREATE_MATCH_QUARTER(HttpStatus.OK, "쿼터 기록 등록 완료", 200),
    UPDATE_MATHC_QUARTER(HttpStatus.OK, "쿼터 기록 수정 완료", 200);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    SuccessMessage(HttpStatus httpStatus, String message, int code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }
}
