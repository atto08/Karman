package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record LineupRecommendResponseDto(
        String description,
        String formation,
        List<LineupPlayerInfo> startingXI
) {

    public static LineupRecommendResponseDto of(String description, String formation, List<LineupPlayerInfo> startingXI) {

        return LineupRecommendResponseDto.builder()
                .description(description)
                .formation(formation)
                .startingXI(startingXI)
                .build();
    }
}
