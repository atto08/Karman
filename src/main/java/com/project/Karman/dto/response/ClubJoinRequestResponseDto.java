package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ClubJoinRequestResponseDto(
        UUID affiliationId,
        String name
) {

    public static ClubJoinRequestResponseDto of(UUID affiliationId, String name) {

        return ClubJoinRequestResponseDto.builder()
                .affiliationId(affiliationId)
                .name(name)
                .build();
    }
}
