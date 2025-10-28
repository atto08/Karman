package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerInfoListResponseDto(
        UUID clubId,
        List<PlayerInfoResponseDto> playerList
) {

    public static PlayerInfoListResponseDto of(UUID clubId, List<PlayerInfoResponseDto> playerList) {

        return PlayerInfoListResponseDto.builder()
                .clubId(clubId)
                .playerList(playerList)
                .build();
    }
}
