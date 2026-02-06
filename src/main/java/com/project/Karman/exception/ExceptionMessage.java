package com.project.Karman.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionMessage {
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러가 발생했습니다.", 500),

    // Member
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", 400),
    PERMISSION_DENIED_MEMBER(HttpStatus.FORBIDDEN, "권한이 없는 유저입니다.", 403),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "찾을 수 없는 유저정보 입니다.", 404),
    DUPLICATED_MEMBER_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다.", 409),

    // Club
    OWNER_CAN_NOT_WITHDRAW_CLUB(HttpStatus.BAD_REQUEST, "구단주는 클럽을 탈퇴할 수 없습니다.", 400), // 탈퇴하면 클럽 삭제 되도록 수정
    ALREADY_JOINED_PLAYER(HttpStatus.BAD_REQUEST, "이미 가입된 선수입니다.", 400),
    NOT_ALLOWED_OWNER_ROLE(HttpStatus.BAD_REQUEST, "구단주 권한은 허용되지 않습니다.", 400),
    PLAYER_HAS_MEMBER_ID(HttpStatus.BAD_REQUEST,"멤버 ID가 존재하는 선수 데이터 입니다.",400),
    NOT_FOUND_CLUB(HttpStatus.NOT_FOUND, "찾을 수 없는 클럽(팀) 입니다.", 404),
    NOT_FOUND_PLAYER_IN_CLUB(HttpStatus.NOT_FOUND, "클럽(팀)에 소속되지 않은 선수 입니다.", 404),
    PERMISSION_DENIED_USER_GET_CLUB(HttpStatus.FORBIDDEN, "클럽 정보 조회 권한이 없는 유저입니다.", 403),
    PERMISSION_DENIED_USER_UPDATE_CLUB(HttpStatus.FORBIDDEN, "클럽 수정 권한이 없는 유저입니다.", 403),

    // Match
    NOT_VALID_FORMATION(HttpStatus.BAD_REQUEST, "등록되지 않은 포메이션입니다.", 400),
    MATCH_NOT_BELONG_TO_CLUB(HttpStatus.BAD_REQUEST, "해당 클럽의 매치정보가 아닙니다.", 400),
    PERMISSION_DENIED_USER_ACCESS_MATCH_DATA(HttpStatus.FORBIDDEN, "클럽 매치정보에 접근 권한이 없는 유저입니다.", 403),
    NOT_FOUND_MATCH(HttpStatus.NOT_FOUND, "찾을 수 없는 매치정보 입니다.", 404),
    NOT_FOUND_MATCH_QUARTER(HttpStatus.NOT_FOUND, "찾을 수 없는 매치의 쿼터정보 입니다.", 404),
    NOT_INCLUDED_PLAYER_IN_LINEUP(HttpStatus.NOT_FOUND, "라인업에 포함되지 않은 선수입니다.", 404),
    PLAYER_NOT_IN_LINEUP_FOR_GOAL_RECORD(HttpStatus.BAD_REQUEST, "골 기록은 라인업에 포함된 선수만 가능합니다.", 400),

    CREATE_AI_RESPONSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ai 응답생성 중 문제가 발생했습니다.", 500);

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    ExceptionMessage(HttpStatus httpStatus, String message, int code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }
}
