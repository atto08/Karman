package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "club")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Club extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID clubId;

    @Column(nullable = false)
    private UUID memberId;

    @Column(nullable = false, length = 30)
    private String clubName;

    @Column(nullable = false, length = 30)
    private String area;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(nullable = false)
    private Date foundationDate;

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Affiliation> affiliationPlayers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();


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

    // 수정시 사용
    public void update(String clubName, String area, AgeGroup ageGroup, Date foundationDate) {
        if (clubName != null) this.clubName = clubName;
        if (area != null) this.area = area;
        if (ageGroup != null) this.ageGroup = ageGroup;
        if (foundationDate != null) this.foundationDate = foundationDate;
    }


    public void addPlayer(Affiliation owner) {
        this.affiliationPlayers.add(owner);
    }
}
