package com.project.Karman.dto.request;

import com.project.Karman.domain.enums.ClubJoinStatus;

public record JoinStatusUpdateRequestDto(
        ClubJoinStatus joinStatus
) {
}
