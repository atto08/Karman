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

    @Column(nullable = false)
    private UUID clubId;

    @Column(nullable = false, length = 50)
    private String opponent;

    @Column(nullable = false)
    private Integer scoredGoal;

    @Column(nullable = false)
    private Integer concededGoal;

    @Column(nullable = false, length = 50)
    private String location;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Weather weather;


    public static Match of(UUID clubId, String opponent, Integer scoredGoal, Integer concededGoal,
                           String location, LocalDateTime matchDate, Weather weather) {

        return Match.builder()
                .clubId(clubId)
                .opponent(opponent)
                .scoredGoal(scoredGoal)
                .concededGoal(concededGoal)
                .location(location)
                .matchDate(matchDate)
                .weather(weather)
                .build();
    }
}
