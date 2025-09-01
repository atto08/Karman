package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "club")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID clubId;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false, length = 50)
    private String clubName;

    @Column(nullable = false, length = 50)
    private String area;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(nullable = false)
    private Date foundationDate;
}
