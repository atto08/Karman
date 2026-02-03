package com.project.Karman.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "match_goal")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID matchGoalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "match_id", referencedColumnName = "match_id", nullable = false),
            @JoinColumn(name = "quarter", referencedColumnName = "quarter", nullable = false)
    })
    private MatchQuarter matchQuarter;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "scorer_name", nullable = false, length = 50)),
            @AttributeOverride(name = "affiliationId", column = @Column(name = "scorer_affiliation_id"))
    })
    private MatchPlayerInfo scorePlayer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "assist_name", length = 50)),
            @AttributeOverride(name = "affiliationId", column = @Column(name = "assist_affiliation_id"))
    })
    private MatchPlayerInfo assistPlayer;


    public static MatchGoal of(MatchQuarter matchQuarter, String scorerName, UUID scorerAffiliationId, String assistPlayerName, UUID assistPlayerAffiliationId) {

        return MatchGoal.builder()
                .matchQuarter(matchQuarter)
                .scorePlayer(MatchPlayerInfo.of(scorerName, scorerAffiliationId))
                .assistPlayer(MatchPlayerInfo.of(assistPlayerName, assistPlayerAffiliationId))
                .build();
    }
}
