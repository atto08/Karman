package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record SearchClubListResponseDto(
        List<SearchClubResponseDto> clubList
) {

    public static SearchClubListResponseDto of(List<SearchClubResponseDto> clubList) {

        return SearchClubListResponseDto.builder()
                .clubList(clubList)
                .build();
    }
}
