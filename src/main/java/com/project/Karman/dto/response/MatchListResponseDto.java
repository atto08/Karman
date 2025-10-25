package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MatchListResponseDto(
        List<MatchSummaryResponseDto> matchList
) {

    public static MatchListResponseDto of(List<MatchSummaryResponseDto> matchList) {

        return MatchListResponseDto.builder()
                .matchList(matchList)
                .build();
    }
}
