package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.Position;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "affiliation")
@NoArgsConstructor
@Getter
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

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(nullable = false)
    private Integer backNumber;

    @Column(nullable = false)
    private Integer matchCount;

    @Column(nullable = false)
    private BigDecimal averageRating;

    @Column(nullable = false)
    private Integer goal;

    @Column(nullable = false)
    private Integer assist;

    @Column(nullable = false)
    private Integer clean;

    @Column(nullable = false)
    private Integer win;

    @Column(nullable = false)
    private Integer draw ;

    @Column(nullable = false)
    private Integer lose ;
}
