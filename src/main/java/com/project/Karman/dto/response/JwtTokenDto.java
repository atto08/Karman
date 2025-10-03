package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record JwtTokenDto(
        String accessToken
) {
    public static JwtTokenDto of(String accessToken) {
        return JwtTokenDto.builder().accessToken(accessToken).build();
    }

    @Override
    public String toString() {
        return accessToken;
    }
}
