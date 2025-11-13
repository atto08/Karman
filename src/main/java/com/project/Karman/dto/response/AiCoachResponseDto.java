package com.project.Karman.dto.response;


import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AiCoachResponseDto(
        String aiCoachResponse
) {

    public static AiCoachResponseDto of(String aiCoachResponse) {

        return AiCoachResponseDto.builder()
                .aiCoachResponse(aiCoachResponse)
                .build();
    }
}
