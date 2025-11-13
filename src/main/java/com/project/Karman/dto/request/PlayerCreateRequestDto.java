package com.project.Karman.dto.request;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import jakarta.validation.constraints.*;

public record PlayerCreateRequestDto(
        @NotBlank(message = "이름을 입력해주세요.")
        @Pattern(regexp = "^[A-Za-z가-힣]{2,20}$",
                message = "이름은 한글 또는 영어로 2~20자 이내여야 합니다.")
        String playerName,

        @NotNull(message = "등번호를 입력해주세요.")
        @Min(1)
        @Max(99)
        Integer backNumber,

        @NotNull(message = "포지션을 입력해주세요.")
        ClubPlayerPosition position
) {
}
