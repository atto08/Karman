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

    @Query("""
                SELECT c
                FROM Club c
                WHERE c.clubName LIKE CONCAT('%', :clubName, '%')
                ORDER BY c.clubName ASC
            """)
    List<Club> findAllClubs(@Param("clubName") String clubName);

    @Query("""
            SELECT a.club FROM Affiliation a
            WHERE a.member.memberId = :memberId
            """)
    List<Club> findAllByMemberId(@Param("memberId") UUID memberId);
}
