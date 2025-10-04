package com.project.Karman.dto.request;

import com.project.Karman.domain.enums.Weather;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record MatchCreateRequestDto(
        @NotBlank(message = "상대 팀 이름은 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣._&·!\\-]+$",
                message = "클럽명은 한글, 영어, 숫자, 일부 특수문자(.-_&·!)만 입력 가능합니다.")
        String opponent,
        @NotNull(message = "득점은 필수 입력 값입니다.")
        @PositiveOrZero
        Integer score,
        @NotNull(message = "실점은 필수 입력 값입니다.")
        @PositiveOrZero
        Integer concededScore,
        @NotBlank(message = "매치 장소는 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9가-힣\\- ]+$",
                message = "위치는 영문, 한글, 숫자, 공백, '-'만 입력 가능합니다.")
        String location,
        @NotNull(message = "날씨는 필수 입력 값입니다.")
        Weather weather) {
}
