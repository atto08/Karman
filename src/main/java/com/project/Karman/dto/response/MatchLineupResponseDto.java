package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchLineupResponseDto(
        UUID affiliationId,
        String playerName,
        ClubPlayerPosition position,
        Integer positionNumber,
        Boolean isSub
) {

    public static MatchLineupResponseDto of(UUID affiliationId,String playerName, ClubPlayerPosition clubPlayerPosition, Integer positionNumber, Boolean isSub) {

        return MatchLineupResponseDto.builder()
                .affiliationId(affiliationId)
                .playerName(playerName)
                .position(clubPlayerPosition)
                .positionNumber(positionNumber)
                .isSub(isSub)
                .build();
    }
}
