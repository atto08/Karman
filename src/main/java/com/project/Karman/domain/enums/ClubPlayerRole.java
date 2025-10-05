package com.project.Karman.domain.enums;

public enum ClubPlayerRole {
    OWNER("구단주"),
    COACH("코치"),
    USER("회원");

    private final String description;

    ClubPlayerRole(String description) {
        this.description = description;
    }
}
