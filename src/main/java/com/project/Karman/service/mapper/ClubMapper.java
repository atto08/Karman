package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.dto.ClubInfoResponseDto;
import com.project.Karman.dto.ClubCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClubMapper {

    public Club toEntity(UUID memberId, ClubCreateRequestDto request) {

        return Club.of(
                memberId,
                request.clubName(),
                request.area(),
                request.ageGroup(),
                request.foundationDate());
    }

    public ClubInfoResponseDto toDto(Club club) {

        return ClubInfoResponseDto.of(
                club.getClubId(),
                club.getClubName(),
                club.getArea(),
                club.getAgeGroup().toString(),
                club.getFoundationDate().toString()
        );
    }
}
