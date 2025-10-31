package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MyClubInfoResponseDto(
        UUID clubId,
        String name
) {

    public static MyClubInfoResponseDto of(UUID clubId, String name) {

        return MyClubInfoResponseDto.builder()
                .clubId(clubId)
                .name(name)
                .build();
    }
}
