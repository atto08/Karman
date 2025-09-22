package com.project.Karman.dto;

import java.util.UUID;

public record SearchClubResponseDto(
        UUID clubId,
        String clubName
) {
}
