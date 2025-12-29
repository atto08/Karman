package com.project.Karman.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AskTacticsRequestDto(
        @NotBlank(message = "카테고리는 필수선택 요소 입니다.")
        String category,

        String formation,

        String subtopic,

        String style,

        String position,

        String question) {
}
