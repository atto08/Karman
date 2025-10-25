package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.Formation;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchGoalCreateRequestDto;
import com.project.Karman.dto.request.MatchLineupCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterCreateRequestDto;
import com.project.Karman.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public MatchQuarter toMatchQuarterEntity(MatchQuarterCreateRequestDto requestDto, Match match, Formation formation) {

        return MatchQuarter.of(
                match.getMatchId(),
                requestDto.quarter(),
                match,
                formation,
                requestDto.concededGoal()
        );
    }

    public MatchLineup toMatchLineupEntity(MatchQuarter matchQuarter, MatchLineupCreateRequestDto lineupRequestDto) {

        return MatchLineup.of(
                matchQuarter,
                lineupRequestDto.positionNumber(),
                lineupRequestDto.position(),
                lineupRequestDto.isSub(),
                lineupRequestDto.name(),
                lineupRequestDto.affiliationId()
        );
    }

    public MatchGoal toMatchGoalEntity(MatchQuarter matchQuarter, MatchGoalCreateRequestDto goalDto) {

        return MatchGoal.of(
                matchQuarter,
                goalDto.scorerName(),
                goalDto.scorerAffiliationId(),
                goalDto.assistPlayerName(),
                goalDto.assistPlayerAffiliationId()
        );
    }

    public MatchResponseDto toMatchDto(Match match) {
        List<MatchQuarterResponseDto> matchQuarterResponseDtoList = match.getMatchQuarters().stream()
                .map(this::toMatchQuarterDto)
                .toList();

        return MatchResponseDto.of(
                match.getMatchId(),
                match.getOpponent(),
                match.getScoredGoal(),
                match.getConcededGoal(),
                match.getLocation(),
                match.getMatchDate(),
                match.getWeather().getDescription(),
                match.getMatchResult().getDescription(),
                matchQuarterResponseDtoList
        );
    }

    public MatchQuarterResponseDto toMatchQuarterDto(MatchQuarter matchQuarter) {

        List<MatchLineupResponseDto> lineupResponseDto = matchQuarter.getLineup().stream()
                .map(this::toMatchLineupDto)
                .toList();

        List<MatchGoalResponseDto> scoredGoalsInfo = matchQuarter.getScoredGoals().stream()
                .map(this::toMatchGoalDto)
                .toList();

        return MatchQuarterResponseDto.of(
                matchQuarter.getMatchQuarterId().getQuarter(),
                matchQuarter.getFormation().getName(),
                (long) scoredGoalsInfo.size(),
                matchQuarter.getConcededGoal(),
                lineupResponseDto,
                scoredGoalsInfo
        );
    }

    public MatchLineupResponseDto toMatchLineupDto(MatchLineup matchLineup) {

        return MatchLineupResponseDto.of(
                matchLineup.getPlayerInfo().getName(),
                matchLineup.getPosition(),
                matchLineup.getPositionNumber(),
                matchLineup.getIsSub()
        );
    }

    public MatchGoalResponseDto toMatchGoalDto(MatchGoal matchGoal) {

        return MatchGoalResponseDto.of(
                matchGoal.getScorePlayer().getName(),
                matchGoal.getAssistPlayer() != null ? matchGoal.getAssistPlayer().getName() : null
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
