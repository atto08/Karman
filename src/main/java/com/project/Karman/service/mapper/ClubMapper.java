package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubAgeGroup;
import com.project.Karman.dto.response.ClubInfoResponseDto;
import com.project.Karman.dto.request.ClubCreateRequestDto;
import com.project.Karman.dto.response.ClubStaticsRecordsResponseDto;
import com.project.Karman.dto.response.MyClubInfoResponseDto;
import com.project.Karman.dto.response.MyClubListResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

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
                club.getClubAgeGroup().getDescription(),
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

    public MyClubListResponseDto toMyClubListDto(List<Club> clubs) {
        List<MyClubInfoResponseDto> clubInfoResponseDtoList = clubs.stream()
                .map(this::toMyClubInfoDto)
                .toList();

        return MyClubListResponseDto.of(clubInfoResponseDtoList);
    }

    public MyClubInfoResponseDto toMyClubInfoDto(Club club) {

        return MyClubInfoResponseDto.of(
                club.getClubId(),
                club.getClubName());
    }
}
