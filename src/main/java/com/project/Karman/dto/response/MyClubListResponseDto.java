package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MyClubListResponseDto(
        List<MyClubInfoResponseDto> clubList
) {

    public static MyClubListResponseDto of(List<MyClubInfoResponseDto> clubList) {

        return MyClubListResponseDto.builder()
                .clubList(clubList)
                .build();
    }
}
