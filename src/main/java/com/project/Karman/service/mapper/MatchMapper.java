package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Match;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.response.MatchListResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public Match toEntity(MatchCreateRequestDto request, Club club) {

        return Match.of(
                club,
                request.opponent(),
                request.scoredGoal(),
                request.concededGoal(),
                request.location(),
                request.matchDate(),
                request.weather()

        );
    }

    public MatchListResponseDto toDto(Match match) {

        return MatchListResponseDto.of(
                match.getMatchId(),
                match.getOpponent(),
                match.getScoredGoal(),
                match.getConcededGoal(),
                match.getMatchDate(),
                match.getLocation()
        );
    }
}
