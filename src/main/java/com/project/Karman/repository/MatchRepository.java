package com.project.Karman.repository;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    @Query("""
            SELECT m FROM Match m
            WHERE m.club = :club
            """)
    List<Match> findAllByClub(@Param("club") Club club);

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
}
