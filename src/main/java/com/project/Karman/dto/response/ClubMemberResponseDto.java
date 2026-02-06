package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ClubMemberResponseDto(
        UUID affiliationId,
        String name,
        Integer backNumber
) {

    public static ClubMemberResponseDto of(UUID affiliationId, String name, Integer backNumber) {

        return ClubMemberResponseDto.builder()
                .affiliationId(affiliationId)
                .name(name)
                .backNumber(backNumber)
                .build();
    }
}
