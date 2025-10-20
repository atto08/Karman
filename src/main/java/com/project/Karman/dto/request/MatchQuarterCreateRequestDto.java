package com.project.Karman.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record MatchQuarterCreateRequestDto(
        @NotNull(message = "쿼터는 필수 입력 값입니다.")
        @Min(value = 1, message = "쿼터는 1 이상이어야 합니다.")
        @Max(value = 8, message = "쿼터는 8 이하여야 합니다.")
        Integer quarter,

        @NotBlank(message = "포메이션은 필수 입력 값입니다.")
        String formation,

        @NotNull(message = "실점은 필수 입력 값입니다.")
        @PositiveOrZero(message = "실점은 0 이상이어야 합니다.")
        Integer concededGoal,

        @NotNull(message = "라인업은 필수 입력 값입니다.")
        @NotEmpty(message = "라인업은 최소 1명 이상이어야 합니다.")
        @Size(min = 1, max = 20, message = "라인업은 1명 이상 20명 이하여야 합니다.")
        @Valid
        List<MatchLineupCreateRequestDto> lineup,

        @Valid
        List<MatchGoalCreateRequestDto> goalsInfo
) {
}
