package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum AgeGroup {
    TEENS_EARLY("10대 초반"),
    TEENS_MID("10대 중반"),
    TEENS_LATE("10대 후반"),
    TWENTIES_EARLY("20대 초반"),
    TWENTIES_MID("20대 중반"),
    TWENTIES_LATE("20대 후반"),
    THIRTIES_EARLY("30대 초반"),
    THIRTIES_MID("30대 중반"),
    THIRTIES_LATE("30대 후반"),
    FORTIES_EARLY("40대 초반"),
    FORTIES_MID("40대 중반"),
    FORTIES_LATE("40대 후반"),
    FIFTIES_EARLY("50대 초반"),
    FIFTIES_MID("50대 중반"),
    FIFTIES_LATE("50대 후반"),
    SIXTIES_EARLY("60대 초반"),
    SIXTIES_MID("60대 중반"),
    SIXTIES_LATE("60대 후반"),
    SEVENTIES_EARLY("70대 초반"),
    SEVENTIES_MID("70대 중반"),
    SEVENTIES_LATE("70대 후반");

    private final String description;

    AgeGroup(String description) {
        this.description = description;
    }
}