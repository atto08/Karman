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
        // 클럽 조회
        Club club = findClub(clubId);
        // 클럽 소속 선수 여부 & 권한 체크
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 매치 객체 생성 & 저장
        Match match = matchMapper.toMatchEntity(requestDto, club);
        matchRepository.save(match);
    }

    @Transactional
    public void createMatchQuarter(MatchQuarterCreateRequestDto requestDto, UUID clubId, UUID matchId, Member member) {
        // [검증 로직]
        // 1) 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 2) 클럽 소속 여부 & 권한 체크 - 운영진 이삼만 쿼터생성 가능
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 3) 매치 조회 - 존재 여부 판단 + 영속성 컨텍스트 저장
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));
        // 4) 클럽 경기 여부
        if (!match.getClub().getClubId().equals(clubId)) {
            throw new CustomException(ExceptionMessage.MATCH_NOT_BELONG_TO_CLUB);
        }
        // TODO - MVP 구현 후 A안 B안 속도 비교
        // 5) 라인업 선수 소속 여부 검증 - A안
        validateAffiliationIdsInSquads(requestDto, clubId);
        // [비즈니스 로직]
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
        updateAffiliationStats(updatedAffiliations);
    }

    private void validateAffiliationIdsInSquads(MatchQuarterCreateRequestDto requestDto, UUID clubId) {
        // 입력받은 affiliationId 목록
        Set<UUID> affiliationIdsToValidate = getAffiliationIdsFromDto(requestDto);
        // 검증
        if (!affiliationIdsToValidate.isEmpty()) {
            // 실존하는 affiliationId 객체만 리스트로 반환
            List<Affiliation> validAffiliations = affiliationRepository
                    .findAllByClub_ClubIdAndAffiliationIds(clubId, new ArrayList<>(affiliationIdsToValidate));
            // 아이디 수가 일치하지 않으면 정상적이지 않은 affiliationId가 포함된 상황
            if (validAffiliations.size() != affiliationIdsToValidate.size()) {
                throw new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB);
            }
        }
    }

    private Set<UUID> getAffiliationIdsFromDto(MatchQuarterCreateRequestDto requestDto) {
        Set<UUID> affiliationIdsToValidate = new HashSet<>();
        // 라인업에서 수집
        for (MatchLineupCreateRequestDto lineupDto : requestDto.lineup()) {
            if (lineupDto.affiliationId() != null) {
                affiliationIdsToValidate.add(lineupDto.affiliationId());
            }
        }
        // 득점/어시스트 정보에서 수집
        if (requestDto.goalsInfo() != null) {
            for (MatchGoalCreateRequestDto goalDto : requestDto.goalsInfo()) {
                if (goalDto.scorerAffiliationId() != null) {
                    affiliationIdsToValidate.add(goalDto.scorerAffiliationId());
                }
                if (goalDto.assistPlayerAffiliationId() != null) {
                    affiliationIdsToValidate.add(goalDto.assistPlayerAffiliationId());
                }
            }
        }
        return affiliationIdsToValidate;
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

    private void updateAffiliationStats(Set<GoalAssistPlayer> updatedAffiliations) {
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
        Club club = findClub(clubId);
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

    private Club findClub(UUID clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
    }

    private void validateUserClubRoleIsManagement(UUID clubId, UUID memberId) {
        // 로그인 유저 클럽 소속여부 확인
        Affiliation loginUser = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, memberId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 권한 체크
        if (loginUser.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
    }
}
