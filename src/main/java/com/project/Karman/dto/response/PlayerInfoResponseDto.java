package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import lombok.AccessLevel;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerInfoResponseDto(
        UUID affiliationId,
        String name,
        Integer backNumber,
        ClubPlayerPosition position,
        Long matchCount,
        Long goal,
        Long assist,
        Long clear,
        BigDecimal point,
        String playerRole
) {

    public static PlayerInfoResponseDto of(UUID affiliationId, String name, Integer backNumber, ClubPlayerPosition clubPlayerPosition,
                                           Long matchCount, Long goal, Long assist, Long clear, BigDecimal point, String playerRole) {

        return PlayerInfoResponseDto.builder()
                .affiliationId(affiliationId)
                .name(name)
                .backNumber(backNumber)
                .position(clubPlayerPosition)
                .matchCount(matchCount)
                .goal(goal)
                .assist(assist)
                .clear(clear)
                .point(point)
                .playerRole(playerRole)
                .build();
    }
}
