package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum MatchResult {
    WIN("승"),
    DRAW("무"),
    LOSE("패");

    private final String description;

    MatchResult(String description) {
        this.description = description;
    }
}
