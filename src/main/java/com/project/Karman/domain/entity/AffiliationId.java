package com.project.Karman.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor  // 기본 생성자
@EqualsAndHashCode  // equals & hashCode 메서드 구현
public class AffiliationId implements Serializable {

    @Column(name = "member_id", columnDefinition = "uuid")
    private UUID memberId;

    @Column(name = "club_id", columnDefinition = "uuid")
    private UUID clubId;
}
