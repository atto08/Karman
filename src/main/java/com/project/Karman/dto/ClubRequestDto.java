package com.project.Karman.dto;

import com.project.Karman.domain.enums.AgeGroup;

import java.util.Date;

public record ClubRequestDto(
        String clubName,
        String area,
        AgeGroup ageGroup,
        Date foundationDate
) {
}
