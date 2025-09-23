package com.project.Karman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDto(
        @NotBlank(message = "이메일(아이디)은 필수 입력 값입니다.")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "올바른 이메일 형식이 아닙니다. (영문, 숫자만 허용)")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9~!@#$%^&*()]{8,20}$",
                message = "비밀번호는 영어 대소문자, 숫자, 특수문자(~!@#$%^&*())를 포함한 최소 8자 ~ 최대 20자 이내로 가능합니다.")
        String password
) {
}
