package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.MatchFormation;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match_quarter")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchQuarter extends BaseEntity {

    @EmbeddedId
    private MatchQuarterId matchQuarterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchFormation matchFormation;

    @Builder.Default
    @Column(nullable = false)
    private Long scoredGoal = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long concededGoal = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "matchQuarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchLineup> lineup = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "matchQuarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchGoal> scoredGoals = new ArrayList<>();


    public static MatchQuarter of(UUID matchId, Integer quarter, Match match, MatchFormation matchFormation) {

        return MatchQuarter.builder()
                .matchQuarterId(MatchQuarterId.of(matchId, quarter))
                .match(match)
                .matchFormation(matchFormation)
                .build();
    }

    public void updateScore(Long scoredGoal, Long concededGoal) {
        this.scoredGoal = scoredGoal;
        this.concededGoal = concededGoal;
    }

    public void updateFormation(MatchFormation formation) {
        this.matchFormation = formation;
    }
}
