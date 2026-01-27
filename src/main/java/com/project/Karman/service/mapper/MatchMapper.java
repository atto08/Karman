package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.MatchFormation;
import com.project.Karman.dto.batch.MatchGoalBatchDto;
import com.project.Karman.dto.batch.MatchLineupBatchDto;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchGoalCreateRequestDto;
import com.project.Karman.dto.request.MatchLineupCreateRequestDto;
import com.project.Karman.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MatchMapper {

    public Match toMatchEntity(MatchCreateRequestDto requestDto, Club club) {

        return Match.of(
                club,
                requestDto.opponent(),
                requestDto.location(),
                requestDto.matchDate(),
                requestDto.weather()
        );
    }

    public MatchQuarter toMatchQuarterEntity(Match match, Integer quarter, MatchFormation matchFormation) {

        return MatchQuarter.of(
                match.getMatchId(),
                quarter,
                match,
                matchFormation
        );
    }

    public MatchLineupBatchDto toMatchLineupBatchDto(UUID lineupId, MatchQuarter matchQuarter, MatchLineupCreateRequestDto lineupRequestDto) {

        return MatchLineupBatchDto.of(
                lineupId,
                matchQuarter,
                lineupRequestDto.positionNumber(),
                lineupRequestDto.position(),
                lineupRequestDto.name(),
                lineupRequestDto.affiliationId(),
                lineupRequestDto.isSub()
        );
    }

    public MatchGoalBatchDto toMatchGoalBatchDto(UUID matchGoalId, MatchQuarter matchQuarter, MatchGoalCreateRequestDto goalRequestDto) {

        return MatchGoalBatchDto.of(
                matchGoalId,
                matchQuarter,
                goalRequestDto.scorerName(),
                goalRequestDto.scorerAffiliationId(),
                goalRequestDto.assistName(),
                goalRequestDto.assistAffiliationId()
        );
    }

    public MatchResponseDto toMatchDto(Match match, Boolean isStaff) {
        List<MatchQuarterResponseDto> matchQuarterResponseDtoList = match.getMatchQuarters().stream()
                .map(this::toMatchQuarterDto)
                .toList();

        return MatchResponseDto.of(
                isStaff,
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
                matchQuarter.getMatchFormation().getName(),
                (long) scoredGoalsInfo.size(),
                matchQuarter.getConcededGoal(),
                lineupResponseDto,
                scoredGoalsInfo
        );
    }

    public MatchLineupResponseDto toMatchLineupDto(MatchLineup matchLineup) {

        return MatchLineupResponseDto.of(
                matchLineup.getPlayerInfo().getAffiliationId(),
                matchLineup.getPlayerInfo().getName(),
                matchLineup.getClubPlayerPosition(),
                matchLineup.getPositionNumber(),
                matchLineup.getIsSub()
        );
    }

    public MatchGoalResponseDto toMatchGoalDto(MatchGoal matchGoal) {

        return MatchGoalResponseDto.of(
                matchGoal.getScorePlayer().getName(),
                matchGoal.getScorePlayer().getAffiliationId() != null ? matchGoal.getScorePlayer().getAffiliationId() : null,
                matchGoal.getAssistPlayer() != null ? matchGoal.getAssistPlayer().getName() : null,
                matchGoal.getAssistPlayer() != null ?
                        (matchGoal.getAssistPlayer().getAffiliationId() != null ? matchGoal.getAssistPlayer().getAffiliationId() : null)
                        : null
        );
    }


    public MatchListResponseDto toMatchListResponseDto(List<Match> matchList, Boolean isStaff) {

        List<MatchSummaryResponseDto> matchSummaryResponseDtoList = matchList.stream()
                .map(this::toMatchSummaryResponseDto)
                .toList();

        return MatchListResponseDto.of(
                isStaff,
                matchSummaryResponseDtoList);
    }

    public MatchSummaryResponseDto toMatchSummaryResponseDto(Match match) {

        return MatchSummaryResponseDto.of(
                match.getMatchId(),
                match.getOpponent(),
                match.getScoredGoal(),
                match.getConcededGoal(),
                match.getMatchDate()
        );
    }
}
