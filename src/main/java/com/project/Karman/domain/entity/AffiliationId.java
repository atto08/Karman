package com.project.Karman.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode  // equals & hashCode 메서드 구현
public class AffiliationId implements Serializable {

    @Column(name = "member_id", columnDefinition = "uuid")
    private UUID memberId;

    @Column(name = "club_id", columnDefinition = "uuid")
    private UUID clubId;


    public static AffiliationId of(UUID memberId, UUID clubId) {

        return AffiliationId.builder()
                .memberId(memberId)
                .clubId(clubId)
                .build();
    }
}
