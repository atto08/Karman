package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.Position;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerSelectResponseDto(
        UUID memberId,
        String name,
        Integer backNumber,
        Position position
) {

    public static PlayerSelectResponseDto of(UUID memberId, String name, Integer backNumber, Position position) {

        return PlayerSelectResponseDto.builder()
                .memberId(memberId)
                .name(name)
                .backNumber(backNumber)
                .position(position)
                .build();
    }
}
