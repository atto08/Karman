package com.project.Karman.repository;

import com.project.Karman.domain.entity.Match;
import com.project.Karman.domain.entity.MatchQuarter;
import com.project.Karman.domain.enums.MatchResult;
import com.project.Karman.repository.projection.ClubMatchStatisticsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    Optional<Match> findByClub_ClubIdAndMatchId(UUID clubId, UUID matchId);

    List<Match> findAllByClub_ClubIdOrderByMatchDateDesc(UUID clubId);

    boolean existsByClub_ClubIdAndMatchId(UUID clubId, UUID matchId);

    @Query("""
            SELECT DISTINCT m
            FROM Match m
            LEFT JOIN FETCH m.matchQuarters mq
            WHERE m.matchId = :matchId
            ORDER BY mq.matchQuarterId.quarter ASC
            """)
    Optional<Match> findByIdWithQuarters(@Param("matchId") UUID matchId);

    @Query("""
            SELECT DISTINCT ml.playerInfo.affiliationId
            FROM MatchLineup ml
            JOIN ml.matchQuarter mq
            WHERE mq.matchQuarterId.matchId = :matchId
                AND ml.playerInfo.affiliationId IS NOT NULL
            """)
    Set<UUID> findPlayedAffiliationIdsByMatchId(@Param("matchId") UUID matchId);

    @Query("""
            SELECT DISTINCT ml.playerInfo.affiliationId
            FROM MatchLineup ml
            WHERE ml.matchQuarter.matchQuarterId.matchId = :matchId
              AND ml.matchQuarter.matchQuarterId.quarter <> :quarter
              AND ml.playerInfo.affiliationId IS NOT NULL
            """)
    Set<UUID> findPlayedAffiliationIdsInOtherQuarters(
            @Param("matchId") UUID matchId,
            @Param("quarter") Integer quarter);

    // matchQuarter Repository 생성 보류
    @Query("""
            SELECT mq
            FROM MatchQuarter mq
            WHERE mq.matchQuarterId.matchId = :matchId
                AND mq.matchQuarterId.quarter = :quarter
            """)
    Optional<MatchQuarter> findByMatchQuarterId_MatchIdAndMatchQuarterId_Quarter(@Param("matchId") UUID matchId,
                                                                                 @Param("quarter") Integer quarter);

    @Query("""
            SELECT
                COUNT(m) as matchCount,
                COALESCE(SUM(CASE WHEN m.matchResult = :win THEN 1 ELSE 0 END), 0) as win,
                COALESCE(SUM(CASE WHEN m.matchResult = :draw THEN 1 ELSE 0 END), 0) as draw,
                COALESCE(SUM(CASE WHEN m.matchResult = :lose THEN 1 ELSE 0 END), 0) as lose,
                COALESCE(SUM(m.scoredGoal), 0) as totalScoreGoal,
                COALESCE(SUM(m.concededGoal), 0) as totalConcedeGoal
            FROM Match m
            WHERE m.club.clubId = :clubId
            """)
    ClubMatchStatisticsProjection findClubMatchStatisticsByClubId(
            @Param("clubId") UUID clubId,
            @Param("win") MatchResult win,
            @Param("draw") MatchResult draw,
            @Param("lose") MatchResult lose);
}
