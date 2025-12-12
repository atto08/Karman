package com.project.Karman.service;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.MatchFormation;
import com.project.Karman.dto.request.*;
import com.project.Karman.dto.response.MatchListResponseDto;
import com.project.Karman.dto.response.MatchResponseDto;
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
        Club club = findClubById(clubId);
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
        checkClubIsExist(clubId);
        // 2) 클럽 소속 여부 & 권한 체크(운영진 이삼만 쿼터생성 가능)
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 3) 매치 조회 - 존재 여부 판단 + 영속성 컨텍스트 저장
        Match match = findMatchById(matchId);
        // 4) 클럽 경기 여부
        checkMatchBelongToClub(match.getClub().getClubId(), clubId);
        // TODO - MVP 구현 후 A안 B안 속도 비교
        // 5) 라인업 선수 소속 여부 검증 - A안
        validateAffiliationIdsInSquad(requestDto.lineup(), requestDto.goalsInfo(), clubId);
        // [비즈니스 로직]
        // 포메이션 변환
        MatchFormation matchFormation = MatchFormation.fromName(requestDto.formation());
        // 쿼터 생성
        MatchQuarter matchQuarter = matchMapper.toMatchQuarterEntity(requestDto, match, matchFormation);
        // 기록 갱신이 필요한 선수들 ID
        Set<UUID> updatedAffiliations = new HashSet<>();
        // 쿼터 라인업 - MatchLineup 테이블 추가
        addQuarterLineup(updatedAffiliations, requestDto.lineup(), matchQuarter);
        // 쿼터 (득점 & 도움) - MatchGoal 테이블 추가
        addQuarterGoalAndAssist(requestDto.goalsInfo(), matchQuarter);
        // 매치에 쿼터 추가
        match.addMatchQuarter(matchQuarter);
        // (득점 & 실점) 계산 - Match 테이블 갱신
        calculateMatchScore(match);
        // (출전 경기수 & 득점 & 도움) 계산 - Affiliation 테이블 갱신
        calculateAffiliationStats(updatedAffiliations);
    }

    @Transactional
    public void updateMatchQuarter(MatchQuarterUpdateRequestDto requestDto, UUID clubId, UUID matchId, Member member, Integer quarter) {
        // [검증 로직]
        // 1) 클럽 조회
        checkClubIsExist(clubId);
        // 2) 클럽 소속 여부 & 권한 체크(운영진 이삼만 쿼터생성 가능)
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 3) 매치 조회 - 존재 여부 판단 + 영속성 컨텍스트 저장
        Match match = findMatchById(matchId);
        // 4) 클럽 경기 여부
        checkMatchBelongToClub(match.getClub().getClubId(), clubId);
        // 5) 라인업 선수 소속 여부 검증
        validateAffiliationIdsInSquad(requestDto.lineup(), requestDto.goalsInfo(), clubId);
        // 쿼터 조회
        MatchQuarter matchQuarter = matchRepository.findByMatchQuarterId_MatchIdAndMatchQuarterId_Quarter(matchId, quarter)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH_QUARTER));
        // 기록 갱신이 필요한 선수들 ID
        Set<UUID> updatedAffiliations = new HashSet<>();
        // 수정전 라인업 포함 선수 ID 추가
        addAffectedAffiliationIds(updatedAffiliations, matchQuarter.getLineup());
        // 포메이션 & 실점 - Match 테이블 갱신
        MatchFormation updateMatchFormation = requestDto.formation() != null ? MatchFormation.fromName(requestDto.formation()) : null;
        matchQuarter.update(updateMatchFormation, requestDto.concededGoal());
        // 기존 정보 제거
        matchQuarter.clearQuarterData();
        // 쿼터 라인업 - MatchLineup 테이블 추가
        addQuarterLineup(updatedAffiliations, requestDto.lineup(), matchQuarter);
        // 쿼터 (득점 & 도움) - MatchGoal 테이블 추가
        addQuarterGoalAndAssist(requestDto.goalsInfo(), matchQuarter);
        // (득점 & 실점) 계산 - Match 테이블 갱신
        calculateMatchScore(match);
        // (출전 경기수 & 득점 & 도움) 계산 - Affiliation 테이블 갱신
        calculateAffiliationStats(updatedAffiliations);
    }

    private void validateAffiliationIdsInSquad(List<MatchLineupCreateRequestDto> lineupRequestDto, List<MatchGoalCreateRequestDto> goalsInfoRequestDto, UUID clubId) {
        // 입력받은 affiliationId 목록
        Set<UUID> affiliationIdsToValidate = getAffiliationIdsFromDto(lineupRequestDto, goalsInfoRequestDto);
        // 검증
        if (!affiliationIdsToValidate.isEmpty()) {
            // 실존하는 affiliationId 객체만 리스트로 반환
            List<Affiliation> validAffiliations = affiliationRepository
                    .findAllByClub_ClubIdAndAffiliationIdIn(clubId, new ArrayList<>(affiliationIdsToValidate));
            // 아이디 수가 일치하지 않으면 정상적이지 않은 affiliationId가 포함된 상황
            if (validAffiliations.size() != affiliationIdsToValidate.size()) {
                throw new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB);
            }
        }
    }

    private Set<UUID> getAffiliationIdsFromDto(List<MatchLineupCreateRequestDto> lineupRequestDto, List<MatchGoalCreateRequestDto> goalsInfoRequestDto) {
        Set<UUID> affiliationIdsToValidate = new HashSet<>();
        // 라인업에서 수집
        for (MatchLineupCreateRequestDto lineupDto : lineupRequestDto) {
            if (lineupDto.affiliationId() != null) {
                affiliationIdsToValidate.add(lineupDto.affiliationId());
            }
        }
        // 득점/어시스트 정보에서 수집
        if (goalsInfoRequestDto != null) {
            for (MatchGoalCreateRequestDto goalDto : goalsInfoRequestDto) {
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

    private void addQuarterLineup(Set<UUID> updatedAffiliations, List<MatchLineupCreateRequestDto> lineup, MatchQuarter matchQuarter) {
        for (MatchLineupCreateRequestDto lineupDto : lineup) {
            // 출전선수 정보 생성
            MatchLineup playerInLineup = matchMapper.toMatchLineupEntity(matchQuarter, lineupDto);
            matchQuarter.addLineup(playerInLineup);
            // 소속 선수
            if (lineupDto.affiliationId() != null) {
                updatedAffiliations.add(lineupDto.affiliationId());
            }
        }
    }

    private void addQuarterGoalAndAssist(List<MatchGoalCreateRequestDto> goalsInfo, MatchQuarter matchQuarter) {

        for (MatchGoalCreateRequestDto goalDto : goalsInfo) {
            // 득점 정보 객체 생성 및 저장
            MatchGoal scoreInfo = matchMapper.toMatchGoalEntity(matchQuarter, goalDto);
            matchQuarter.addScoredGoal(scoreInfo);
        }
    }

    private void calculateAffiliationStats(Set<UUID> updateAffiliationIds) {
        for (UUID updateAffiliationId : updateAffiliationIds) {
            Affiliation updatePlayer = affiliationRepository.findById(updateAffiliationId)
                    .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));

            Long updatedMatches = matchRepository.countPlayedMatchesByAffiliationId(updateAffiliationId);
            Long updatedGoals = matchRepository.countGoalsByAffiliationId(updateAffiliationId);
            Long updatedAssists = matchRepository.countAssistsByAffiliationId(updateAffiliationId);
            updatePlayer.updateMatchCount(updatedMatches);
            updatePlayer.updateGoal(updatedGoals);
            updatePlayer.updateAssist(updatedAssists);
        }
    }

    private void addAffectedAffiliationIds(Set<UUID> updatedAffiliations, List<MatchLineup> matchLineup) {

        for (MatchLineup ml : matchLineup) {
            if (ml.getPlayerInfo().getAffiliationId() != null) {
                updatedAffiliations.add(ml.getPlayerInfo().getAffiliationId());
            }
        }
    }

    @Transactional(readOnly = true)
    public MatchListResponseDto getMatchInfoAll(Member member, UUID clubId) {
        // 클럽 조회
        checkClubIsExist(clubId);
        // 클럽 전체 매치 기록 조회
        List<Match> matchList = matchRepository.findAllByClub_ClubIdOrderByMatchDateDesc(clubId);
        // 로그인 유저 운영진(Owner or Coach) 여부 판단
        Boolean isStaff = validateManagementPermission(clubId, member.getMemberId());

        return matchMapper.toMatchListResponseDto(matchList, isStaff);
    }

    @Transactional(readOnly = true)
    public MatchResponseDto getMatchInfo(Member member, UUID clubId, UUID matchId) {
        // 클럽 조회
        checkClubIsExist(clubId);
        // 매치 조회
        Match match = findMatchById(matchId);
        // 클럽 경기 여부
        checkMatchBelongToClub(match.getClub().getClubId(), clubId);
        // 로그인 유저 클럽 소속 여부 체크
        if(!isMemberOfClub(clubId, member.getMemberId())) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB);
        }
        // 로그인 유저 운영진(Owner or Coach) 여부 판단
        Boolean isStaff = validateManagementPermission(clubId, member.getMemberId());
        // Dto 반환
        return matchMapper.toMatchDto(match, isStaff);
    }

    private Club findClubById(UUID clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
    }

    private void checkClubIsExist(UUID clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
    }

    private Match findMatchById(UUID matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));
    }

    private void checkMatchBelongToClub(UUID matchClubId, UUID clubId) {
        if (!matchClubId.equals(clubId)) {
            throw new CustomException(ExceptionMessage.MATCH_NOT_BELONG_TO_CLUB);
        }
    }

    private Boolean isMemberOfClub(UUID clubId, UUID memberId) {
        return affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatus(
                clubId,
                memberId,
                ClubJoinStatus.APPROVED);
    }

    private Boolean validateManagementPermission(UUID clubId, UUID memberId) {
        return affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndPlayerRoleIn(
                clubId,
                memberId,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH));
    }

    private void validateUserClubRoleIsManagement(UUID clubId, UUID memberId) {
        // 로그인 유저 클럽 소속여부 체크
        if (!isMemberOfClub(clubId, memberId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB);
        }
        // 클럽에서 로그인 유저 권한 체크
        if (!validateManagementPermission(clubId, memberId)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_USER_ACCESS_MATCH_DATA);
        }
    }
}
