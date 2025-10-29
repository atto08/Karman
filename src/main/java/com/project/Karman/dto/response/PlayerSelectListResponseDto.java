package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerSelectListResponseDto(
        UUID clubId,
        List<PlayerSelectResponseDto> playerList
) {

    public static PlayerSelectListResponseDto of(UUID clubId, List<PlayerSelectResponseDto> playerList) {

        return PlayerSelectListResponseDto.builder()
                .clubId(clubId)
                .playerList(playerList)
                .build();
    }
}
