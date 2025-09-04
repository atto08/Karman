package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum Position {
    FW("공격수"),
    CF("중앙 공격수"),
    WF("윙어"),
    MF("미드필더"),
    CM("중앙 미드필더"),
    CAM("공격형 미드필더"),
    CDM("수비형 미드필더"),
    DF("수비수"),
    WB("윙백"),
    FB("풀백"),
    CB("센터백"),
    GK("골키퍼");

    private final String description;

    Position(String description) {
        this.description = description;
    }
}
