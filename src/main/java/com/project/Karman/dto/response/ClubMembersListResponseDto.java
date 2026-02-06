package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record ClubMembersListResponseDto(
        List<ClubMemberResponseDto> clubMembers
) {

    public static ClubMembersListResponseDto of(List<ClubMemberResponseDto> clubMembers) {

        return ClubMembersListResponseDto.builder()
                .clubMembers(clubMembers)
                .build();
    }
}
