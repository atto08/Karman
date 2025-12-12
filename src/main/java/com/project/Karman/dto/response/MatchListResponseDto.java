package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MatchListResponseDto(
        Boolean isStaff,
        List<MatchSummaryResponseDto> matchList
) {

    public static MatchListResponseDto of(Boolean isStaff, List<MatchSummaryResponseDto> matchList) {

        return MatchListResponseDto.builder()
                .isStaff(isStaff)
                .matchList(matchList)
                .build();
    }
}
