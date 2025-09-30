package com.project.Karman.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record ClubUpdateRequestDto(
        @Pattern(regexp = "^[a-zA-Z0-9가-힣._&·!\\-]+$",
                message = "클럽명은 한글, 영어, 숫자, 일부 특수문자(.-_&·!)만 입력 가능합니다.")
        @Size(min = 1, max = 30, message = "클럽명은 1자 이상 30자 이하여야 합니다.")
        String clubName,

        @Pattern(regexp = "^[a-zA-Z0-9가-힣._&·!\\-]+$",
                message = "지역은 한글, 영어만 입력 가능합니다.")
        @Size(min = 1, max = 30, message = "지역은 1자 이상 30자 이하여야 합니다.")
        String area,

        @Pattern(regexp = "^[0-9]{1,3}대-(초반|중반|후반)$",
                message = "연령대는 '10대-초반', '20대-중반', '30대-후반' 형식으로 입력해야 합니다.")
        String ageGroup,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date foundationDate
) {
}
