package com.project.Karman.repository;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.enums.ClubJoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, UUID> {

    @Query("""
            SELECT a FROM Affiliation a
            WHERE a.club.clubId = :clubId AND a.affiliationId IN :affiliationIds
            """)
    List<Affiliation> findAllByClub_ClubIdAndAffiliationIds(@Param("clubId") UUID clubId,
                                                            @Param("affiliationIds") List<UUID> affiliationIds
    );

    @Query("""
            SELECT a FROM Affiliation a
            WHERE a.club.clubId = :clubId AND a.join_status = :joinStatus
            """)
    List<Affiliation> findAllByClub_ClubId(@Param("clubId") UUID clubId,
                                           @Param("joinStatus") ClubJoinStatus joinStatus);

    @Query("""
            SELECT a FROM Affiliation a
            WHERE a.club.clubId = :clubId AND a.member.memberId = :memberId
            """)
    Optional<Affiliation> findByClub_ClubIdAndMember_MemberId(@Param("clubId") UUID clubId,
                                                              @Param("memberId") UUID memberId);
}
