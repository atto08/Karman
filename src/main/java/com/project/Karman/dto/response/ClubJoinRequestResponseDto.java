package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ClubJoinRequestResponseDto(
        UUID affiliationId,
        UUID memberId,
        String name
) {

    public static ClubJoinRequestResponseDto of(UUID affiliationId, UUID memberId, String name) {

        return ClubJoinRequestResponseDto.builder()
                .affiliationId(affiliationId)
                .memberId(memberId)
                .name(name)
                .build();
    }
}
