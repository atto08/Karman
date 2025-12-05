package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record SearchClubResponseDto(
        UUID clubId,
        String clubName,
        String area
) {

    public static SearchClubResponseDto of(UUID clubId, String clubName, String area) {

        return SearchClubResponseDto.builder()
                .clubId(clubId)
                .clubName(clubName)
                .area(area)
                .build();
    }
}
