package com.project.Karman.dto;

import com.project.Karman.domain.enums.AgeGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record ClubCreateRequestDto(
        @NotBlank(message = "클럽명은 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣._&·!\\-]+$",
                message = "클럽명은 한글, 영어, 숫자, 일부 특수문자(.-_&·!)만 입력 가능합니다.")
        @Size(min = 1, max = 50, message = "클럽명은 1자 이상 50자 이하여야 합니다.")
        String clubName,

        @NotBlank(message = "지역은 필수 입력 값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣._&·!\\-]+$",
                message = "지역은 한글, 영어만 입력 가능합니다.")
        @Size(min = 1, max = 50, message = "지역은 1자 이상 50자 이하여야 합니다.")
        String area,

        @NotBlank(message = "연령대는 필수 입력 값입니다.")
        @Pattern(regexp = "^[0-9]{1,3}대-(초반|중반|후반)$",
                message = "연령대는 '10대-초반', '20대-중반', '70대-후반' 형식으로 입력해야 합니다.")
        AgeGroup ageGroup,

        @NotBlank(message = "창단일은 필수 입력 값입니다.")
        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
                message = "창단일은 yyyy-MM-dd 형식으로 입력해야 합니다.")
        Date foundationDate
) {
}
