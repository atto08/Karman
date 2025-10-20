package com.project.Karman.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MatchQuarterId implements Serializable {

    @Column(name = "match_id", columnDefinition = "uuid", nullable = false)
    private UUID matchId;

    @Column(columnDefinition = "integer", nullable = false, unique = true)
    private Integer quarter;


    public static MatchQuarterId of(UUID matchId, Integer quarter) {

        return MatchQuarterId.builder()
                .matchId(matchId)
                .quarter(quarter)
                .build();
    }
}
