package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.ClubPlayerPosition;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "match_lineup")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchLineup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID matchLineupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "match_id", referencedColumnName = "match_id", nullable = false),
            @JoinColumn(name = "quarter", referencedColumnName = "quarter", nullable = false)
    })
    private MatchQuarter matchQuarter;

    @Column(columnDefinition = "integer", nullable = false)
    private Integer positionNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClubPlayerPosition clubPlayerPosition;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isSub = false;

    @Embedded
    @Column(nullable = false)
    private MatchPlayerInfo playerInfo;

    public static MatchLineup of(MatchQuarter matchQuarter, Integer positionNumber, ClubPlayerPosition clubPlayerPosition, Boolean isSub, String name, UUID affiliationId) {

        return MatchLineup.builder()
                .matchQuarter(matchQuarter)
                .positionNumber(positionNumber)
                .clubPlayerPosition(clubPlayerPosition)
                .isSub(isSub)
                .playerInfo(MatchPlayerInfo.of(name, affiliationId))
                .build();
    }
}
