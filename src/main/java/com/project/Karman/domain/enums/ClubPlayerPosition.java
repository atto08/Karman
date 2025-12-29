package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum ClubPlayerPosition {
    GK("골키퍼"),
    DF("수비수"),
    CB("센터백"),
    LB_RB("풀백"),
    LWB_RWB("윙백"),
    MF("미드필더"),
    CDM("수비형 미드필더"),
    CM("중앙 미드필더"),
    CAM("공격형 미드필더"),
    FW("공격수"),
    CF("중앙 포워드"),
    LW_RW("윙어"),
    ST("스트라이커");

    private final String description;

    ClubPlayerPosition(String description) {
        this.description = description;
    }
}
