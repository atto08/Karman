package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum ClubJoinStatus {
    APPROVED("가입 승인"),
    PENDING("가입 진행 중"),
    REJECTED("가입 거부");

    private final String description;

    ClubJoinStatus(String description) {
        this.description = description;
    }
}
