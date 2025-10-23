package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchListResponseDto(
        UUID matchId,
        String opponent,
        Long scoredGoal,
        Long concededGoal,
        LocalDateTime matchDate,
        String location
) {


    public static MatchListResponseDto of(UUID matchId, String opponent, Long scoredGoal, Long concededGoal, LocalDateTime matchDate, String location) {

        return MatchListResponseDto.builder()
                .matchId(matchId)
                .opponent(opponent)
                .scoredGoal(scoredGoal)
                .concededGoal(concededGoal)
                .matchDate(matchDate)
                .location(location)
                .build();
    }
}
