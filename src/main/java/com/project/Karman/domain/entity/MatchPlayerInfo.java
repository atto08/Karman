package com.project.Karman.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchPlayerInfo {

    @Column(nullable = false)
    private String name;

    @Column
    private UUID affiliationId;

    public static MatchPlayerInfo of(String name, UUID affiliationId) {

        return MatchPlayerInfo.builder()
                .name(name)
                .affiliationId(affiliationId)
                .build();
    }
}
