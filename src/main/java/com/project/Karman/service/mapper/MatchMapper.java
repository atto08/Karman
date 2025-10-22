package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.Formation;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchGoalCreateRequestDto;
import com.project.Karman.dto.request.MatchLineupCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterCreateRequestDto;
import com.project.Karman.dto.response.MatchListResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public Match toMatchEntity(MatchCreateRequestDto requestDto, Club club) {

        return Match.of(
                club,
                requestDto.opponent(),
                requestDto.scoredGoal(),
                requestDto.concededGoal(),
                requestDto.location(),
                requestDto.matchDate(),
                requestDto.weather()
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

    public MatchQuarter toMatchQuarterEntity(MatchQuarterCreateRequestDto requestDto, Match match, Formation formation) {

        return MatchQuarter.of(
                match.getMatchId(),
                requestDto.quarter(),
                match,
                formation,
                requestDto.concededGoal()
        );
    }

    public MatchLineup toMatchQuarterLineupEntity(MatchQuarter matchQuarter, MatchLineupCreateRequestDto lineupRequestDto) {

        return MatchLineup.of(
                matchQuarter,
                lineupRequestDto.positionNumber(),
                lineupRequestDto.position(),
                lineupRequestDto.isSub(),
                lineupRequestDto.name(),
                lineupRequestDto.affiliationId()
        );
    }

    public MatchGoal toMatchQuarterGoalEntity(MatchQuarter matchQuarter, MatchGoalCreateRequestDto goalDto) {

        return MatchGoal.of(
                matchQuarter,
                goalDto.scorerName(),
                goalDto.scorerAffiliationId(),
                goalDto.assistPlayerName(),
                goalDto.assistPlayerAffiliationId()
        );
    }
}
