package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ClubInfoResponseDto(
        UUID clubId,
        String clubName,
        String area,
        String ageGroup,
        String foundationDate
) {

    public static ClubInfoResponseDto of(UUID clubId, String clubName, String area, String ageGroup, String foundationDate) {

        return ClubInfoResponseDto.builder()
                .clubId(clubId)
                .clubName(clubName)
                .area(area)
                .ageGroup(ageGroup)
                .foundationDate(foundationDate)
                .build();
    }
}
