package com.project.Karman.dto.request;

import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.ClubPlayerPosition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PlayerStatUpdateRequestDto(
        ClubPlayerPosition position,
        @Min(1)
        @Max(99)
        Integer backNumber,
        ClubPlayerRole playerRole
) {
}
