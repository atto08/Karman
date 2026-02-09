package com.project.Karman.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TransferPlayerRecordRequestDto(
        @NotNull(message = "이관할 멤버의 ID는 필수 입력 값 입니다.")
        UUID memberId,

        @NotNull(message = "이관될 선수의 ID는 필수 입력 값 입니다.")
        UUID affiliationId
) {
}
