package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.MatchResult;
import com.project.Karman.domain.enums.Weather;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "match_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private Club club;

    @Column(nullable = false, length = 50)
    private String opponent;

    @Builder.Default
    @Column(nullable = false)
    private Long scoredGoal = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long concededGoal = 0L;

    @Builder.Default
    @Column(length = 50)
    private String location = null;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Builder.Default
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Weather weather = Weather.SUNNY;

    @Builder.Default
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private MatchResult matchResult = MatchResult.DRAW;

    @Builder.Default
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("matchQuarterId.quarter ASC")
    private List<MatchQuarter> matchQuarters = new ArrayList<>();


    public static Match of(Club club, String opponent, String location, LocalDateTime matchDate, Weather weather) {

        return Match.builder()
                .club(club)
                .opponent(opponent)
                .location(location)
                .matchDate(matchDate)
                .weather(weather)
                .build();
    }

    // 매치에 쿼터기록 추가
    public void addMatchQuarter(MatchQuarter matchQuarter) {
        matchQuarters.add(matchQuarter);
    }

    public void updateScore(Long updateScoredGoal, Long updatedConcededGoal) {
        this.scoredGoal += updateScoredGoal;
        this.concededGoal += updatedConcededGoal;

        if (this.scoredGoal > this.concededGoal) {
            this.matchResult = MatchResult.WIN;
        } else if (this.scoredGoal < this.concededGoal) {
            this.matchResult = MatchResult.LOSE;
        } else {
            this.matchResult = MatchResult.DRAW;
        }
    }
}