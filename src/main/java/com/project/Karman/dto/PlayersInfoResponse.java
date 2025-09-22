package com.project.Karman.dto;

import com.project.Karman.domain.enums.Position;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record PlayersInfoResponse(
        UUID memberId,
        String name,
        Integer backNumber,
        Position position
) {

    public static PlayersInfoResponse of(UUID memberId, String name, Integer backNumber, Position position) {

        return PlayersInfoResponse.builder()
                .memberId(memberId)
                .name(name)
                .backNumber(backNumber)
                .position(position)
                .build();
    }
}
