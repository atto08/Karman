package com.project.Karman.dto.batch;

import com.project.Karman.domain.entity.MatchPlayerInfo;
import com.project.Karman.domain.entity.MatchQuarter;
import com.project.Karman.domain.enums.ClubPlayerPosition;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchLineupBatchDto(
        UUID matchLineupId,
        UUID matchId,
        Integer quarter,
        Integer positionNumber,
        ClubPlayerPosition position,
        MatchPlayerInfo playerInfo,
        Boolean isSub
) {

    public static MatchLineupBatchDto of(UUID lineupId, MatchQuarter matchQuarter, Integer positionNumber,
                                         ClubPlayerPosition position, String name, UUID affiliationId, Boolean isSub) {

        return MatchLineupBatchDto.builder()
                .matchLineupId(lineupId)
                .matchId(matchQuarter.getMatchQuarterId().getMatchId())
                .quarter(matchQuarter.getMatchQuarterId().getQuarter())
                .positionNumber(positionNumber)
                .position(position)
                .playerInfo(com.project.Karman.domain.entity.MatchPlayerInfo.of(name, affiliationId))
                .isSub(isSub)
                .build();
    }
}
