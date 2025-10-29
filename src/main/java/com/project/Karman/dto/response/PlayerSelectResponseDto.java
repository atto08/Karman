package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerSelectResponseDto(
        UUID memberId,
        String name,
        Integer backNumber,
        ClubPlayerPosition position
) {

    public static PlayerSelectResponseDto of(UUID memberId, String name, Integer backNumber, ClubPlayerPosition clubPlayerPosition) {

        return PlayerSelectResponseDto.builder()
                .memberId(memberId)
                .name(name)
                .backNumber(backNumber)
                .position(clubPlayerPosition)
                .build();
    }
}
