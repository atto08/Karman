package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ClubJoinRequestListResponseDto(
        UUID clubId,
        List<ClubJoinRequestResponseDto> playerList
) {

    public static ClubJoinRequestListResponseDto of(UUID clubId, List<ClubJoinRequestResponseDto> playerList) {

        return ClubJoinRequestListResponseDto.builder()
                .clubId(clubId)
                .playerList(playerList)
                .build();
    }
}
