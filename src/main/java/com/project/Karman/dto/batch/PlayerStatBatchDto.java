package com.project.Karman.dto.batch;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerStatBatchDto(
        UUID affiliationId,
        long matchCount,
        long goal,
        long assist
) {

    public static PlayerStatBatchDto of(UUID affiliationId, long matchCount, long goal, long assist) {

        return PlayerStatBatchDto.builder()
                .affiliationId(affiliationId)
                .matchCount(matchCount)
                .goal(goal)
                .assist(assist)
                .build();
    }
}
