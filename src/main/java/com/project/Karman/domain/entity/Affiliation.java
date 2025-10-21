package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.Position;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "affiliation")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Affiliation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "affiliation_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID affiliationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", columnDefinition = "uuid", nullable = false, updatable = false)
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

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClubPlayerRole playerRole;

    @Builder.Default
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClubJoinStatus joinStatus = ClubJoinStatus.PENDING;


    public static Affiliation of(Member member, Club club, ClubPlayerRole playerRole) {
        ClubJoinStatus status = playerRole.equals(ClubPlayerRole.OWNER) ? ClubJoinStatus.APPROVED : ClubJoinStatus.PENDING;

        return Affiliation.builder()
                .member(member)
                .club(club)
                .playerRole(playerRole)
                .joinStatus(status)
                .build();
    }

    public void updateJoinStatus(ClubJoinStatus updatedStatus) {
        this.joinStatus = updatedStatus;
    }

    public void updatePlayerInfo(Position updatedPosition, Integer updatedBackNumber, ClubPlayerRole updatedPlayerRole) {
        this.position = updatedPosition == null ? this.position : updatedPosition;
        this.backNumber = updatedBackNumber == null ? this.backNumber : updatedBackNumber;
        this.playerRole = updatedPlayerRole == null ? this.playerRole : updatedPlayerRole;
    }
}
