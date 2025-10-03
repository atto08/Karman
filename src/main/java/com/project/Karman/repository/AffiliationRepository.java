package com.project.Karman.repository;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.AffiliationId;
import com.project.Karman.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, AffiliationId> {

    @Query("""
            SELECT a FROM Affiliation a
            WHERE a.club.clubId = :clubId AND a.member.memberId IN :memberIds
            """)
    List<Affiliation> findAllByClubIdAndMemberIds(
            @Param("clubId") UUID clubId,
            @Param("memberIds") List<UUID> memberIds
    );

    List<Affiliation> findAllByClub(Club club);

    @Query("""
            SELECT a FROM Affiliation a
            WHERE a.club.clubId = :clubId AND a.member.memberId = :memberId
            """)
    Optional<Affiliation> findByClubIdAndMemberId(UUID clubId, UUID memberId);
}
