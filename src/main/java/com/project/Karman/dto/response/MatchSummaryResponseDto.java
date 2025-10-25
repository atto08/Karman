package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchSummaryResponseDto(
        UUID matchId,
        String opponent,
        Long scoredGoal,
        Long concededGoal,
        LocalDateTime matchDate
) {


    public static MatchSummaryResponseDto of(UUID matchId, String opponent, Long scoredGoal, Long concededGoal, LocalDateTime matchDate) {

        return MatchSummaryResponseDto.builder()
                .matchId(matchId)
                .opponent(opponent)
                .scoredGoal(scoredGoal)
                .concededGoal(concededGoal)
                .matchDate(matchDate)
                .build();
    }
}
