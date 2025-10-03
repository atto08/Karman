package com.project.Karman.dto.response;

import java.util.UUID;

public record SearchClubResponseDto(
        UUID clubId,
        String clubName
) {
}
