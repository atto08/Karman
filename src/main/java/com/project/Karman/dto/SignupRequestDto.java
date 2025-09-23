package com.project.Karman.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record SignupRequestDto(
        @NotBlank(message = "이메일(아이디)은 필수 입력 값입니다.")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "올바른 이메일 형식이 아닙니다. (영문, 숫자만 허용)")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9~!@#$%^&*()]{8,20}$",
                message = "비밀번호는 영어 대소문자, 숫자, 특수문자(~!@#$%^&*())를 포함한 최소 8자 ~ 최대 20자 이내로 가능합니다.")
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9~!@#$%^&*()]{8,20}$",
                message = "비밀번호 확인은 비밀번호와 동일한 조건을 만족해야 합니다.")
        String passwordCheck,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z가-힣]{2,20}$",
                message = "이름은 한글 또는 영어로 2~20자 이내여야 합니다.")
        String name,

        @NotNull(message = "나이는 필수 입력 값입니다.")
        @Min(value = 1, message = "나이는 1살 이상만 가능합니다.")
        @Max(value = 120, message = "나이는 120 이하만 가능합니다.")
        Integer age,

        @NotNull(message = "몸무게는 필수 입력 값입니다.")
        @Positive(message = "몸무게는 양수여야 합니다.")
        BigDecimal weight,

        @NotNull(message = "키는 필수 입력 값입니다.")
        @Positive(message = "키는 양수여야 합니다.")
        BigDecimal height
) {
}
