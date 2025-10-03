package com.project.Karman.dto;

import com.project.Karman.domain.enums.ClubJoinStatus;

public record JoinStatusUpdateRequestDto(
        ClubJoinStatus joinStatus
) {
}
