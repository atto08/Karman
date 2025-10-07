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
    OWNER_CAN_NOT_WITHDRAW_CLUB(HttpStatus.BAD_REQUEST, "구단주는 클럽을 탈퇴할 수 없습니다.",400), // 탈퇴하면 클럽 삭제 되도록 수정
    ALREADY_JOINED_PLAYER(HttpStatus.BAD_REQUEST,"이미 가입된 선수입니다.", 400),
    NOT_FOUND_CLUB(HttpStatus.NOT_FOUND, "찾을 수 없는 클럽(팀) 입니다.", 404),
    NOT_FOUND_PLAYER_IN_CLUB(HttpStatus.NOT_FOUND, "클럽(팀)에 소속되지 않은 선수 입니다.", 404),

    // Match
    NOT_FOUND_MATCH(HttpStatus.NOT_FOUND, "찾을 수 없는 매치정보 입니다.", 404);
    private final HttpStatus httpStatus;
    private final String message;
    private final int code;

    ExceptionMessage(HttpStatus httpStatus, String message, int code) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }
}
