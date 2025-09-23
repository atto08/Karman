package com.project.Karman.repository;

import com.project.Karman.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
