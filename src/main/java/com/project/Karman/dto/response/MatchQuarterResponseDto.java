package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MatchQuarterResponseDto(
        Integer quarter,
        String formation,
        Long scoredGoal,
        Long concededGoal,
        List<MatchLineupResponseDto> lineup,
        List<MatchGoalResponseDto> scoredPlayers
) {
    public static MatchQuarterResponseDto of(Integer quarter, String formation, Long scoredGoal, Long concededGoal,
                                             List<MatchLineupResponseDto> lineup, List<MatchGoalResponseDto> scoredPlayers) {

        return MatchQuarterResponseDto.builder()
                .quarter(quarter)
                .formation(formation)
                .scoredGoal(scoredGoal)
                .concededGoal(concededGoal)
                .lineup(lineup)
                .scoredPlayers(scoredPlayers)
                .build();
    }
}
