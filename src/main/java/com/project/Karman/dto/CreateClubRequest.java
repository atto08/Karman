package com.project.Karman.dto;

import com.project.Karman.domain.enums.AgeGroup;

import java.util.Date;
import java.util.UUID;

public record CreateClubRequest(
        UUID memberId,
        String clubName,
        String area,
        AgeGroup ageGroup,
        Date foundationDate
) {
}
