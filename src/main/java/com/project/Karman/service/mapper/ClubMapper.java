package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubAgeGroup;
import com.project.Karman.dto.response.ClubInfoResponseDto;
import com.project.Karman.dto.request.ClubCreateRequestDto;
import com.project.Karman.dto.response.ClubStaticsRecordsResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {

    public Club toClubEntity(Member member, ClubCreateRequestDto request) {

        return Club.of(
                member,
                request.clubName(),
                request.area(),
                ClubAgeGroup.fromDescription(request.ageGroup()),
                request.foundationDate());
    }

    public ClubInfoResponseDto toClubInfoDto(Club club) {

        return ClubInfoResponseDto.of(
                club.getClubId(),
                club.getClubName(),
                club.getArea(),
                club.getClubAgeGroup().toString(),
                club.getFoundationDate().toString()
        );
    }

    public ClubStaticsRecordsResponseDto toClubStaticsRecordsDto(Long matchCount, Long win, Long draw, Long lose, Long scoreGoals, Long concedeGoals) {

        return ClubStaticsRecordsResponseDto.of(
                matchCount,
                win,
                draw,
                lose,
                scoreGoals,
                concedeGoals);
    }
}
