package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum Formation {
    FOUR_THREE_THREE("4-3-3"),
    FOUR_FOUR_TWO("4-4-2"),
    FOUR_TWO_THREE_ONE("4-2-3-1"),
    FOUR_ONE_TWO_ONE_TWO("4-1-2-1-2"),
    FOUR_THREE_TWO_ONE("4-3-2-1"),
    FOUR_FIVE_ONE("4-5-1"),
    FOUR_ONE_FOUR_ONE("4-1-4-1"),
    FOUR_ONE_THREE_TWO("4-1-3-2"),
    FOUR_THREE_ONE_TWO("4-3-1-2"),
    THREE_FOUR_THREE("3-4-3"),
    THREE_FIVE_TWO("3-5-2"),
    FIVE_THREE_TWO("5-3-2"),
    NOT_VALID_FORMATION("존재하지 않는 포메이션");

    private final String name;

    Formation(String name) {
        this.name = name;
    }

    public static Formation fromName(String name) {
        for (Formation formation : Formation.values()) {
            if (formation.getName().equals(name)) {
                return formation;
            }
        }

        return NOT_VALID_FORMATION;
    }
}
