package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.Position;
import lombok.AccessLevel;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerInfoResponseDto(
        String name,
        Integer backNumber,
        Position position,
        Long matchCount,
        Long goal,
        Long assist,
        Long clear,
        BigDecimal point,
        String playerRole
) {

    public static PlayerInfoResponseDto of(String name, Integer backNumber, Position position, Long matchCount,
                                           Long goal, Long assist, Long clear, BigDecimal point, String playerRole) {

        return PlayerInfoResponseDto.builder()
                .name(name)
                .backNumber(backNumber)
                .position(position)
                .matchCount(matchCount)
                .goal(goal)
                .assist(assist)
                .clear(clear)
                .point(point)
                .playerRole(playerRole)
                .build();
    }
}
