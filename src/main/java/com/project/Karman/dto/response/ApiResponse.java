package com.project.Karman.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

public record ApiResponse<T>(
        @NotNull String message,
        @NotNull int code,
        @JsonInclude(JsonInclude.Include.NON_NULL) T data) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, 200, data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(message, 200, null);
    }
}
