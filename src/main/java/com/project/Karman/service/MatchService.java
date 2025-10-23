package com.project.Karman.service;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.Formation;
import com.project.Karman.domain.enums.GoalAssist;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchGoalCreateRequestDto;
import com.project.Karman.dto.request.MatchLineupCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterCreateRequestDto;
import com.project.Karman.dto.response.GoalAssistPlayer;
import com.project.Karman.dto.response.MatchListResponseDto;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import com.project.Karman.repository.MatchRepository;
import com.project.Karman.service.mapper.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final ClubRepository clubRepository;
    private final MatchRepository matchRepository;
    private final AffiliationRepository affiliationRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public void createMatch(MatchCreateRequestDto requestDto, UUID clubId, Member member) {
        // 클럽 소속 선수 여부
        Affiliation player = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 권한 체크
        if (player.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 클럽 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
        // 매치 객체 생성 & 저장
        Match match = matchMapper.toMatchEntity(requestDto, club);
        matchRepository.save(match);
    }

    @Transactional
    public void createMatchQuarter(MatchQuarterCreateRequestDto requestDto, UUID clubId, UUID matchId, Member member) {
        // 클럽 소속 선수 여부
        Affiliation loginUser = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 권한 체크 - 운영진 이삼만 쿼터생성 가능
        if (loginUser.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 매치 조회 - 영속성 컨텍스트 저장
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));
        // 포메이션 변환
        Formation formation = Formation.fromName(requestDto.formation());
        // 쿼터 생성
        MatchQuarter matchQuarter = matchMapper.toMatchQuarterEntity(requestDto, match, formation);
        // 쿼터 라인업 추가
        addQuarterLineup(requestDto.lineup(), matchQuarter);
        // 쿼터 득점 or 어시스트 MatchGoal 테이블 데이터 추가 - 기록 업데이트 필요한 선수들 정보
        Set<GoalAssistPlayer> updatedAffiliations = addGoalOrAssistInfo(requestDto.goalsInfo(), matchQuarter);
        // 매치에 쿼터 추가
        match.addMatchQuarter(matchQuarter);
        // 득점 계산 - Match 테이블 필드
        calculateMatchScore(match);
        // 득점 & 도움 계산 - Affiliation 테이블 업데이트
        calculateUpdatedGoalOrAssist(updatedAffiliations);
    }

    private void calculateMatchScore(Match match) {
        // 득점
        Long updatedScoredGoal = matchRepository.countGoalsByMatchId(match.getMatchId());
        // 실점
        Long updatedConcededGoal = matchRepository.countConcededGoalsByMatchId(match.getMatchId());
        // 업데이트 스코어
        match.updateScore(updatedScoredGoal, updatedConcededGoal);
    }

    private void addQuarterLineup(List<MatchLineupCreateRequestDto> lineup, MatchQuarter matchQuarter) {
        for (MatchLineupCreateRequestDto lineupDto : lineup) {
            // 출전선수 정보 생성
            MatchLineup playerInLineup = matchMapper.toMatchQuarterLineupEntity(matchQuarter, lineupDto);
            matchQuarter.addLineup(playerInLineup);
        }
    }

    private Set<GoalAssistPlayer> addGoalOrAssistInfo(List<MatchGoalCreateRequestDto> goalsInfo, MatchQuarter matchQuarter) {
        Set<GoalAssistPlayer> updatedAffiliations = new HashSet<>();

        for (MatchGoalCreateRequestDto goalDto : goalsInfo) {
            // 득점 정보 객체 생성 및 저장
            MatchGoal scoreInfo = matchMapper.toMatchQuarterGoalEntity(matchQuarter, goalDto);
            matchQuarter.addScoredGoal(scoreInfo);
            // 득점한 선수가 소속 선수
            if (goalDto.scorerAffiliationId() != null) {
                updatedAffiliations.add(new GoalAssistPlayer(goalDto.scorerAffiliationId(), GoalAssist.GOAL));
            }
            // 어시스트 한 선수가 소속 선수
            if (goalDto.assistPlayerAffiliationId() != null) {
                updatedAffiliations.add(new GoalAssistPlayer(goalDto.assistPlayerAffiliationId(), GoalAssist.ASSIST));
            }
        }

        return updatedAffiliations;
    }

    private void calculateUpdatedGoalOrAssist(Set<GoalAssistPlayer> updatedAffiliations) {
        for (GoalAssistPlayer goalAssistPlayer : updatedAffiliations) {
            Affiliation updatePlayer = affiliationRepository.findById(goalAssistPlayer.affiliationId())
                    .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
            // 득점 경우
            if (goalAssistPlayer.type().equals(GoalAssist.GOAL)) {
                Long updatedGoals = matchRepository.countGoalsByAffiliationId(goalAssistPlayer.affiliationId());
                updatePlayer.updateGoal(updatedGoals);
            }
            // 도움 경우
            else {
                Long updatedAssists = matchRepository.countAssistsByAffiliationId(goalAssistPlayer.affiliationId());
                updatePlayer.updateAssist(updatedAssists);
            }
        }
    }


    @Transactional(readOnly = true)
    public List<MatchListResponseDto> getMatchAll(Member member, UUID clubId) {
        // 클럽 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
        // 클럽 아이디를 갖고 있는 경기 기록 전부 조회
        List<Match> matchList = matchRepository.findAllByClub(club);
        // dto로 변환
        List<MatchListResponseDto> matchAllDto = new ArrayList<>();
        for (Match match : matchList) {
            MatchListResponseDto matchDto = matchMapper.toDto(match);
            matchAllDto.add(matchDto);
        }

        return matchAllDto;
    }
}
