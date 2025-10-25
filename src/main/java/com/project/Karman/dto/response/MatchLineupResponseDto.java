package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.Position;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchLineupResponseDto(
        String playerName,
        Position position,
        Integer positionNumber,
        Boolean isSub
) {

    public static MatchLineupResponseDto of(String playerName, Position position, Integer positionNumber, Boolean isSub) {

        return MatchLineupResponseDto.builder()
                .playerName(playerName)
                .position(position)
                .positionNumber(positionNumber)
                .isSub(isSub)
                .build();
    }
}
