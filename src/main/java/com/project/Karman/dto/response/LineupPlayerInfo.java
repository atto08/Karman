package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.ClubPlayerPosition;

import java.util.UUID;

public record LineupPlayerInfo(
        UUID affiliationId,
        String name,
        ClubPlayerPosition position,
        Integer backNumber
) {
}
