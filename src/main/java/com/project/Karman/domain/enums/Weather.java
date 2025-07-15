package com.project.Karman.domain.enums;

import lombok.Getter;

@Getter
public enum Weather {
    SUNNY("맑음"),
    CLOUDY("흐림"),
    RAINY("비"),
    SNOWY("눈");

    private final String description;

    Weather(String description) {
        this.description = description;
    }
}