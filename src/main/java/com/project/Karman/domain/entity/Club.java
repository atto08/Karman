package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.ClubAgeGroup;
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
    @Column(name = "club_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID clubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private Member member;

    @Column(nullable = false, length = 30)
    private String clubName;

    @Column(nullable = false, length = 30)
    private String area;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ClubAgeGroup clubAgeGroup;

    @Column(nullable = false)
    private Date foundationDate;

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Affiliation> affiliationPlayers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();


    // 엔터티 생성시 사용
    public static Club of(Member member, String clubName, String area, ClubAgeGroup clubAgeGroup, Date foundationDate) {

        return Club.builder()
                .member(member)
                .clubName(clubName)
                .area(area)
                .clubAgeGroup(clubAgeGroup)
                .foundationDate(foundationDate)
                .build();
    }

    // 수정시 사용
    public void update(String clubName, String area, ClubAgeGroup clubAgeGroup, Date foundationDate) {
        if (clubName != null) this.clubName = clubName;
        if (area != null) this.area = area;
        if (clubAgeGroup != null) this.clubAgeGroup = clubAgeGroup;
        if (foundationDate != null) this.foundationDate = foundationDate;
    }


    public void addPlayer(Affiliation affiliation) {
        this.affiliationPlayers.add(affiliation);
    }
}
