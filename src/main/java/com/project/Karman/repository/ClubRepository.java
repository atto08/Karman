package com.project.Karman.repository;

import com.project.Karman.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {

    List<Club> findByClubNameContainingOrderByClubNameAsc(String clubName);

    @Query("""
            SELECT DISTINCT c FROM Club c
            JOIN c.affiliationPlayers ap
            WHERE ap.member.memberId = :memberId
            AND ap.joinStatus = 'APPROVED'
            """)
    List<Club> findClubsByMemberBelongsTo(@Param("memberId") UUID memberId);
}
