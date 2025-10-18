package com.project.Karman.dto.request;

import com.project.Karman.domain.enums.Position;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record MatchLineupCreateRequestDto(
        @NotNull(message = "포지션 번호는 필수 입력 값입니다.")
        @Min(value = 1, message = "포지션 번호는 1 이상이어야 합니다.")
        @Max(value = 11, message = "포지션 번호는 11 이하여야 합니다.")
        Integer positionNumber,

        @NotNull(message = "포지션은 필수 입력 값입니다.")
        Position position,

        UUID affiliationId,

        @NotBlank(message = "선수 이름은 필수 입력 값입니다.")
        @Size(min = 1, max = 50, message = "선수 이름은 1자 이상 50자 이하여야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "선수 이름은 한글, 영어, 숫자만 입력 가능합니다.")
        String name,

        @NotNull(message = "교체 선수 여부는 필수 입력 값입니다.")
        Boolean isSub
) {
}
