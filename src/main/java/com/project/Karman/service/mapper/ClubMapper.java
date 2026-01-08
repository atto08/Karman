package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubAgeGroup;
import com.project.Karman.dto.response.*;
import com.project.Karman.dto.request.ClubCreateRequestDto;
import com.project.Karman.repository.projection.ClubMatchStatisticsProjection;
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

    public ClubInfoResponseDto toClubInfoDto(Club club, Boolean isAssociated) {

        return ClubInfoResponseDto.of(
                club.getClubId(),
                club.getClubName(),
                club.getArea(),
                club.getClubAgeGroup().getDescription(),
                club.getFoundationDate().toString(),
                isAssociated);
    }

    public ClubStatisticsRecordsResponseDto toClubStatisticsRecordsDto(ClubMatchStatisticsProjection clubStatistics) {

        return ClubStatisticsRecordsResponseDto.of(
                clubStatistics.getMatchCount(),
                clubStatistics.getWin(),
                clubStatistics.getDraw(),
                clubStatistics.getLose(),
                clubStatistics.getTotalScoreGoal(),
                clubStatistics.getTotalConcedeGoal());
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

    public SearchClubListResponseDto toSearchClubListDto(List<Club> clubs) {
        List<SearchClubResponseDto> searchClubResponseDtoList = clubs.stream()
                .map(this::toSearchClubDto)
                .toList();

        return SearchClubListResponseDto.of(searchClubResponseDtoList);
    }

    public SearchClubResponseDto toSearchClubDto(Club club) {

        return SearchClubResponseDto.of(
                club.getClubId(),
                club.getClubName(),
                club.getArea());
    }
}
