package com.project.Karman.dto.batch;

import com.project.Karman.domain.entity.MatchPlayerInfo;
import com.project.Karman.domain.entity.MatchQuarter;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchGoalBatchDto(
        UUID matchGoalId,
        UUID matchId,
        Integer quarter,
        MatchPlayerInfo scorer,
        MatchPlayerInfo assist
) {

    public static MatchGoalBatchDto of(UUID matchGoalId, MatchQuarter matchQuarter,
                                       String scorerName, UUID scorerId, String assistName, UUID assistId) {

        return MatchGoalBatchDto.builder()
                .matchGoalId(matchGoalId)
                .matchId(matchQuarter.getMatchQuarterId().getMatchId())
                .quarter(matchQuarter.getMatchQuarterId().getQuarter())
                .scorer(MatchPlayerInfo.of(scorerName, scorerId))
                .assist(MatchPlayerInfo.of(assistName, assistId))
                .build();
    }
}
