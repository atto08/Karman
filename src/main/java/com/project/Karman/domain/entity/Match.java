package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.Weather;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer score;

    @Column(nullable = false)
    private Integer concededScore;

    @Column(nullable = false, length = 50)
    private String location;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Weather weather;


    public static Match of(UUID clubId, String opponent, Integer score, Integer concededScore, String location, Weather weather) {

        return Match.builder()
                .clubId(clubId)
                .opponent(opponent)
                .score(score)
                .concededScore(concededScore)
                .location(location)
                .weather(weather)
                .build();
    }
}
