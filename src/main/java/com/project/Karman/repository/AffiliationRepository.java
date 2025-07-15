package com.project.Karman.repository;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.AffiliationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, AffiliationId> {
    List<Affiliation> findByClub_ClubName(String clubName);
}
