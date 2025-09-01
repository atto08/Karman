package com.project.Karman.repository;

import com.project.Karman.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
}
