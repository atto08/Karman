package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchLineupResponseDto(
        String playerName,
        ClubPlayerPosition position,
        Integer positionNumber,
        Boolean isSub
) {

    public static MatchLineupResponseDto of(String playerName, ClubPlayerPosition clubPlayerPosition, Integer positionNumber, Boolean isSub) {

        return MatchLineupResponseDto.builder()
                .playerName(playerName)
                .position(clubPlayerPosition)
                .positionNumber(positionNumber)
                .isSub(isSub)
                .build();
    }
}
