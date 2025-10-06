package com.project.Karman.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.Karman.exception.CustomException;
import jakarta.validation.constraints.NotNull;

public record ErrorResponse<T>(
        @NotNull String message,
        @NotNull int code,
        @JsonInclude(JsonInclude.Include.NON_NULL) T data) {

    public static ErrorResponse<Object> error(CustomException e) {
        return new ErrorResponse<>(e.getMessage(), e.getCode(), null);
    }

    public static ErrorResponse<Void> error(String message, int code) {
        return new ErrorResponse<>(message, code, null);
    }
}
