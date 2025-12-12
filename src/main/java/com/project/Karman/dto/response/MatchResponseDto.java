package com.project.Karman.dto.response;


import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchResponseDto(
        Boolean isStaff,
        UUID matchId,
        String opponent,
        Long totalScoredGoal,
        Long totalConcededGoal,
        String location,
        LocalDateTime matchDate,
        String weather,
        String matchResult,
        List<MatchQuarterResponseDto> matchQuarters
) {

    public static MatchResponseDto of(Boolean isStaff, UUID matchId, String opponent, Long totalScoredGoal, Long totalConcededGoal,
                                      String location, LocalDateTime matchDate, String weather, String matchResult, List<MatchQuarterResponseDto> matchQuarters) {

        return MatchResponseDto.builder()
                .isStaff(isStaff)
                .matchId(matchId)
                .opponent(opponent)
                .totalScoredGoal(totalScoredGoal)
                .totalConcededGoal(totalConcededGoal)
                .location(location)
                .matchDate(matchDate)
                .weather(weather)
                .matchResult(matchResult)
                .matchQuarters(matchQuarters)
                .build();
    }
}
