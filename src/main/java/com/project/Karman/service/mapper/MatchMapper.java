package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Match;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MatchMapper {

    public Match toEntity(MatchCreateRequestDto request, UUID clubId) {

        return Match.of(
                clubId,
                request.opponent(),
                request.score(),
                request.concededScore(),
                request.location(),
                request.weather()

        );
    }
}
