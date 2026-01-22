package com.project.Karman.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MatchGoalCreateRequestDto(
        @NotBlank(message = "득점자 이름은 필수 입력 값입니다.")
        @Size(min = 1, max = 50, message = "득점자 이름은 1자 이상 50자 이하여야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "득점자 이름은 한글, 영어, 숫자만 입력 가능합니다.")
        String scorerName,

        UUID scorerAffiliationId,

        @Size(max = 50, message = "어시스트 선수 이름은 50자 이하여야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "어시스트 선수 이름은 한글, 영어, 숫자만 입력 가능합니다.")
        String assistName,

        UUID assistAffiliationId
) {
}
