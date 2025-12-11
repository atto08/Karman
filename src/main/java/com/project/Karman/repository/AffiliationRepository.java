package com.project.Karman.repository;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.enums.ClubJoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, UUID> {

    List<Affiliation> findAllByClub_ClubIdAndAffiliationIdIn(UUID clubId, List<UUID> affiliationIds);

    List<Affiliation> findAllByClub_ClubIdAndJoinStatusOrderByBackNumberAsc(UUID clubId, ClubJoinStatus joinStatus);

    Optional<Affiliation> findByClub_ClubIdAndMember_MemberId(UUID clubId, UUID memberId);

    Boolean existsByClub_ClubIdAndMember_MemberIdAndJoinStatus(UUID clubId, UUID memberId, ClubJoinStatus joinStatus);
}
