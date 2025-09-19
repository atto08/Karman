package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.dto.CreateClubRequest;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {

    public Club toEntity(CreateClubRequest request) {

        return Club.of(
                request.memberId(),
                request.clubName(),
                request.area(),
                request.ageGroup(),
                request.foundationDate());
    }
}
