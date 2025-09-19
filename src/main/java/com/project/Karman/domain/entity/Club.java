package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "club")
@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    // 엔터티 생성시 사용
    public static Club of(UUID memberId, String clubName, String area, AgeGroup ageGroup, Date foundationDate) {

        return Club.builder()
                .memberId(memberId)
                .clubName(clubName)
                .area(area)
                .ageGroup(ageGroup)
                .foundationDate(foundationDate)
                .build();
    }
}
