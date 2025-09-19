package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.dto.ClubRequestDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClubMapper {

    public Club toEntity(UUID memberId, ClubRequestDto request) {

        return Club.of(
                memberId,
                request.clubName(),
                request.area(),
                request.ageGroup(),
                request.foundationDate());
    }
}
