package com.project.Karman.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record MatchGoalResponseDto(
        String scorerPlayer,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID scorerPlayerAffiliationId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String assistPlayer,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID assistPlayerAffiliationId
) {

    public static MatchGoalResponseDto of(String scorerPlayer, UUID scorerPlayerAffiliationId, String assistPlayer, UUID assistPlayerAffiliationId) {

        return MatchGoalResponseDto.builder()
                .scorerPlayer(scorerPlayer)
                .scorerPlayerAffiliationId(scorerPlayerAffiliationId)
                .assistPlayer(assistPlayer)
                .assistPlayerAffiliationId(assistPlayerAffiliationId)
                .build();
    }
}
