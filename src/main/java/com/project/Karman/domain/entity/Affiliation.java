package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.Position;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "affiliation")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Affiliation extends BaseEntity {

    @EmbeddedId
    private AffiliationId affiliationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("clubId")
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder.Default
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Position position = Position.GK;

    @Builder.Default
    @Column(nullable = false)
    private Integer backNumber = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer matchCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer goal = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer assist = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer clear = 0;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal point = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private ClubJoinStatus joinStatus = ClubJoinStatus.PENDING;


    public static Affiliation of(Member member, Club club) {

        return Affiliation.builder()
                .affiliationId(AffiliationId.of(member.getMemberId(), club.getClubId()))
                .member(member)
                .club(club)
                .build();
    }

    public void updateJoinStatus(ClubJoinStatus updatedStatus) {
        this.joinStatus = updatedStatus;
    }
}
