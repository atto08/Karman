package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum Position {
    FW("공격수"),
    MF("미드필더"),
    DF("수비수"),
    GK("골키퍼");

    private final String description;

    Position(String description) {
        this.description = description;
    }
}
