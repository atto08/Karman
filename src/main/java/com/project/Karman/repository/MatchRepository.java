package com.project.Karman.repository;

import com.project.Karman.domain.entity.Match;
import com.project.Karman.domain.entity.MatchQuarter;
import com.project.Karman.domain.enums.MatchResult;
import com.project.Karman.dto.response.ClubStaticsRecordsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findAllByClub_ClubIdOrderByMatchDateDesc(UUID clubId);

    @Query("""
            SELECT DISTINCT m FROM Match m
            LEFT JOIN FETCH m.matchQuarters mq
            WHERE m.matchId = :matchId
            ORDER BY mq.matchQuarterId.quarter ASC
            """)
    Optional<Match> findByIdWithQuarters(@Param("matchId") UUID matchId);

    @Query("""
            SELECT COUNT(mg) FROM MatchGoal mg
            WHERE mg.matchQuarter.matchQuarterId.matchId = :matchId
            """)
    Long countGoalsByMatchId(@Param("matchId") UUID matchId);

    @Query("""
            SELECT COALESCE(SUM(mq.concededGoal), 0) FROM MatchQuarter mq
            WHERE mq.matchQuarterId.matchId = :matchId
            """)
    Long countConcededGoalsByMatchId(@Param("matchId") UUID matchId);

    @Query("""
            SELECT COUNT(mg) FROM MatchGoal mg
            WHERE mg.scorePlayer.affiliationId = :affiliationId
            """)
    Long countGoalsByAffiliationId(@Param("affiliationId") UUID affiliationId);

    @Query("""
            SELECT COUNT(mg) FROM MatchGoal mg
            WHERE mg.assistPlayer.affiliationId = :affiliationId
            """)
    Long countAssistsByAffiliationId(@Param("affiliationId") UUID affiliationId);

    @Query("""
            SELECT COUNT(DISTINCT(ml.matchQuarter.matchQuarterId.matchId)) FROM MatchLineup ml
            WHERE ml.playerInfo.affiliationId = :affiliationId
            """)
    Long countPlayedMatchesByAffiliationId(@Param("affiliationId") UUID affiliationId);

    // matchQuarter Repository 생성 보류
    @Query("""
            SELECT mq FROM MatchQuarter mq
            WHERE mq.matchQuarterId.matchId = :matchId
            AND mq.matchQuarterId.quarter = :quarter
            """)
    Optional<MatchQuarter> findByMatchQuarterId_MatchIdAndMatchQuarterId_Quarter(@Param("matchId") UUID matchId,
                                                                                 @Param("quarter") Integer quarter);

    @Query("""
            SELECT COUNT(m),
                SUM(CASE WHEN m.matchResult = :win THEN 1 ELSE 0 END),
                SUM(CASE WHEN m.matchResult = :draw THEN 1 ELSE 0 END),
                SUM(CASE WHEN m.matchResult = :lose THEN 1 ELSE 0 END),
                COALESCE(SUM(m.scoredGoal), 0),
                COALESCE(SUM(m.concededGoal), 0)
            FROM Match m
            WHERE m.club.clubId = :clubId
            """)
    ClubStaticsRecordsResponseDto findClubMatchStatisticsByClubId(
            UUID clubId,
            @Param("win") MatchResult win,
            @Param("draw") MatchResult draw,
            @Param("lose") MatchResult lose);
}
