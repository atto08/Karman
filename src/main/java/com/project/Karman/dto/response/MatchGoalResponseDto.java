package com.project.Karman.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MatchGoalResponseDto(
        String scorerPlayer,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String assistPlayer
) {

    public static MatchGoalResponseDto of(String scorerPlayer, String assistPlayer) {

        return MatchGoalResponseDto.builder()
                .scorerPlayer(scorerPlayer)
                .assistPlayer(assistPlayer)
                .build();
    }
}
