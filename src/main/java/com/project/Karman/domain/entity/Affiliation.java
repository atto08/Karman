package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.ClubPlayerPosition;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.vo.PlayerStatsDelta;
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
    @JoinColumn(name = "club_id", columnDefinition = "uuid", nullable = false, updatable = false)
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "uuid")
    private Member member;

    @Column(nullable = false, length = 50)
    private String playerName;

    @Column(nullable = false)
    private Integer backNumber;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClubPlayerPosition playerPosition;

    @Builder.Default
    @Column(nullable = false)
    private Long matchCount = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long goal = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long assist = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long clear = 0L;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal point = BigDecimal.valueOf(0);

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClubPlayerRole playerRole;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ClubJoinStatus joinStatus;


    public static Affiliation of(Club club, Member member, String playerName, Integer backNumber,
                                 ClubPlayerPosition playerPosition, ClubPlayerRole playerRole, ClubJoinStatus joinStatus) {

        return Affiliation.builder()
                .club(club)
                .member(member)
                .playerName(playerName)
                .backNumber(backNumber)
                .playerPosition(playerPosition)
                .playerRole(playerRole)
                .joinStatus(joinStatus)
                .build();
    }

    public void updateJoinStatus(ClubJoinStatus updatedStatus) {
        this.joinStatus = updatedStatus;
    }

    public void updatePlayerInfo(ClubPlayerPosition updatedPlayerPosition, Integer updatedBackNumber) {
        this.playerPosition = updatedPlayerPosition == null ? this.playerPosition : updatedPlayerPosition;
        this.backNumber = updatedBackNumber == null ? this.backNumber : updatedBackNumber;
    }

    public void applyDelta(PlayerStatsDelta delta) {
        this.matchCount += delta.getMatchCount();
        this.goal += delta.getGoal();
        this.assist += delta.getAssist();
    }
}
