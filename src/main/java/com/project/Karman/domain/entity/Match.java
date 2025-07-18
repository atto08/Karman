package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "match")
@NoArgsConstructor
@Getter
public class Match extends BaseEntity {

    @Id
    @UuidGenerator
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
    @Enumerated(EnumType.STRING)
    private Weather weather;
}
