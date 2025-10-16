package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.Weather;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", columnDefinition = "uuid")
    private Club club;

    @Column(nullable = false, length = 50)
    private String opponent;

    @Builder.Default
    @Column(nullable = false)
    private Integer scoredGoal = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer concededGoal = 0;

    @Builder.Default
    @Column(length = 50)
    private String location = null;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Builder.Default
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Weather weather = Weather.SUNNY;


    public static Match of(Club club, String opponent, Integer scoredGoal, Integer concededGoal,
                           String location, LocalDateTime matchDate, Weather weather) {

        return Match.builder()
                .club(club)
                .opponent(opponent)
                .scoredGoal(scoredGoal)
                .concededGoal(concededGoal)
                .location(location)
                .matchDate(matchDate)
                .weather(weather)
                .build();
    }
}
