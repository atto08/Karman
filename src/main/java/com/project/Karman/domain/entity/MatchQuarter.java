package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.Formation;
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

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Formation formation = Formation.FOUR_THREE_THREE;

    @Builder.Default
    @Column(nullable = false)
    private Integer scoredGoal = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer concededGoal = 0;

    @Builder.Default
    @OneToMany(mappedBy = "matchQuarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchLineup> lineup = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "matchQuarter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchGoal> scoredGoals = new ArrayList<>();


    public static MatchQuarter of(UUID matchId, Integer quarter, Match match, Formation formation, Integer concededGoal) {

        return MatchQuarter.builder()
                .matchQuarterId(MatchQuarterId.of(matchId, quarter))
                .match(match)
                .formation(formation)
                .concededGoal(concededGoal)
                .build();
    }

    public void addLineup(MatchLineup player) {
        lineup.add(player);
    }

    public void addScoredGoal(MatchGoal goal) {
        scoredGoals.add(goal);
    }

    public void countGoal() {
        scoredGoal = scoredGoals.size();
    }
}
