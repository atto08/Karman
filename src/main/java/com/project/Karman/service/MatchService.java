package com.project.Karman.service;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.MatchFormation;
import com.project.Karman.domain.vo.MatchScoreDelta;
import com.project.Karman.domain.vo.PlayerStatsDelta;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);
    private final ClubRepository clubRepository;
    private final MatchRepository matchRepository;
    private final AffiliationRepository affiliationRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public void createMatch(MatchCreateRequestDto requestDto, UUID clubId, Member member) {
        // 클럽 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
        // 클럽 소속 선수 여부 & 권한 체크
        if (!affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatusAndPlayerRoleIn(
                clubId,
                member.getMemberId(),
                ClubJoinStatus.APPROVED,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH))) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 매치 객체 생성 & 저장
        Match match = matchMapper.toMatchEntity(requestDto, club);
        matchRepository.save(match);
    }

    @Transactional
    public void createMatchQuarter(MatchQuarterCreateRequestDto requestDto, UUID clubId, UUID matchId, Member member) {
        // 1) 클럽 존재
        checkClubIsExist(clubId);

        // 2) 권한(운영진)
        if (!affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatusAndPlayerRoleIn(
                clubId,
                member.getMemberId(),
                ClubJoinStatus.APPROVED,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH))) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }

        // 3) 매치 조회 - 클럽 매치 여부 통합(4)
        Match match = matchRepository.findByClub_ClubIdAndMatchId(clubId, matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));

        // 5) 라인업/골에 포함된 affiliationId가 클럽 소속선수 ID인지 검증
        validateAffiliationIdsInSquad(requestDto.lineup(), requestDto.goalsInfo(), clubId);

        // 6) matchId 기준으로 "이미 출전한 선수" Set 확보 (matchCount 중복 증가 방지)
        Set<UUID> playedAffiliationIds = matchRepository.findPlayedAffiliationIdsByMatchId(matchId);

        // 7) 쿼터 생성
        MatchQuarter matchQuarter = matchMapper.toMatchQuarterEntity(
                match, requestDto.quarter(), MatchFormation.fromName(requestDto.formation()));

        // 8) 델타 준비
        MatchScoreDelta matchScoreDelta = new MatchScoreDelta(requestDto.goalsInfo().size(), requestDto.concededGoal());
        Map<UUID, PlayerStatsDelta> playerStatsDeltaMap = new HashMap<>();

        // 9) 라인업 저장 + (matchCountDelta 계산)
        for (MatchLineupCreateRequestDto playerInfo : requestDto.lineup()) {
            MatchLineup matchLineup = matchMapper.toMatchLineupEntity(matchQuarter, playerInfo);
            matchQuarter.addLineup(matchLineup);

            UUID affiliationId = playerInfo.affiliationId();
            if (affiliationId == null) continue;

            // deltaMap 엔트리 확보
            playerStatsDeltaMap.putIfAbsent(affiliationId, new PlayerStatsDelta(0, 0, 0));

            // 해당 match에서 처음 출전이면 matchCount +1
            if (!playedAffiliationIds.contains(affiliationId)) {
                playerStatsDeltaMap.get(affiliationId).addMatchCount();
                playedAffiliationIds.add(affiliationId); // 같은 요청에서 중복 방지
            }
        }

        // 10) 득점/도움 저장 + (goal/assistDelta 계산)
        for (MatchGoalCreateRequestDto goalInfo : requestDto.goalsInfo()) {
            MatchGoal matchGoal = matchMapper.toMatchGoalEntity(matchQuarter, goalInfo);
            matchQuarter.addScoredGoal(matchGoal);

            UUID scorerId = goalInfo.scorerAffiliationId();
            if (scorerId != null) {
                if (!playerStatsDeltaMap.containsKey(scorerId)) {
                    throw new CustomException(ExceptionMessage.PLAYER_NOT_IN_LINEUP_FOR_GOAL_RECORD);
                }
                playerStatsDeltaMap.get(scorerId).addGoal();
            }

            UUID assistId = goalInfo.assistPlayerAffiliationId();
            if (assistId != null) {
                if (!playerStatsDeltaMap.containsKey(assistId)) {
                    throw new CustomException(ExceptionMessage.PLAYER_NOT_IN_LINEUP_FOR_GOAL_RECORD);
                }
                playerStatsDeltaMap.get(assistId).addAssist();
            }
        }

        matchQuarter.updateScore(matchScoreDelta.getScoreGoal(), matchScoreDelta.getConcedeGoal());

        // 11) match에 쿼터 추가
        match.addMatchQuarter(matchQuarter);

        // 12) Match 스코어 증분 반영
        match.addScore(matchScoreDelta.getScoreGoal(), matchScoreDelta.getConcedeGoal());

        // 13) Affiliation 스탯 증분 반영 (한 번에 조회 후 더티체킹)
        applyAffiliationDeltas(playerStatsDeltaMap);
    }

    private void applyAffiliationDeltas(Map<UUID, PlayerStatsDelta> deltaMap) {
        if (deltaMap.isEmpty()) return;

        List<UUID> deltaIds = new ArrayList<>(deltaMap.keySet());
        List<Affiliation> affiliations = affiliationRepository.findAllById(deltaIds);

        Map<UUID, Affiliation> affiliationMap = new HashMap<>();
        for (Affiliation affiliation : affiliations) {
            affiliationMap.put(affiliation.getAffiliationId(), affiliation);
        }

        for (Map.Entry<UUID, PlayerStatsDelta> entry : deltaMap.entrySet()) {
            UUID affiliationId = entry.getKey();
            PlayerStatsDelta delta = entry.getValue();

            Affiliation affiliation = affiliationMap.get(affiliationId);
            if (affiliation == null) continue;

            affiliation.applyDelta(delta);
        }
    }

    @Transactional
    public void updateMatchQuarter(MatchQuarterUpdateRequestDto requestDto, UUID clubId, UUID matchId, Member member, Integer quarter) {

    }

    private void validateAffiliationIdsInSquad(List<MatchLineupCreateRequestDto> lineupRequestDto, List<MatchGoalCreateRequestDto> goalsInfoRequestDto, UUID clubId) {
        // 입력받은 affiliationId 목록
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

    @Transactional(readOnly = true)
    public MatchListResponseDto getMatchInfoAll(Member member, UUID clubId) {
        // 클럽 조회
        checkClubIsExist(clubId);
        // 클럽 전체 매치 기록 조회
        List<Match> matchList = matchRepository.findAllByClub_ClubIdOrderByMatchDateDesc(clubId);
        // 로그인 유저 운영진(Owner or Coach) 여부 판단
        Boolean isStaff = affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatusAndPlayerRoleIn(
                clubId,
                member.getMemberId(),
                ClubJoinStatus.APPROVED,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH));

        return matchMapper.toMatchListResponseDto(matchList, isStaff);
    }

    @Transactional(readOnly = true)
    public MatchResponseDto getMatchInfo(Member member, UUID clubId, UUID matchId) {
        // 클럽 조회
        checkClubIsExist(clubId);
        // 클럽 경기 여부 판단
        if (!matchRepository.existsByClub_ClubIdAndMatchId(clubId, matchId)) {
            throw new CustomException(ExceptionMessage.MATCH_NOT_BELONG_TO_CLUB);
        }
        // 로그인 유저 소속 여부 판단
        Affiliation loginUser = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.PERMISSION_DENIED_USER_GET_CLUB));
        // 승인 처리된 선수만 가능
        if (loginUser.getJoinStatus() != ClubJoinStatus.APPROVED) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_USER_GET_CLUB);
        }
        // 매치 조회
        Match match = matchRepository.findByIdWithQuarters(matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));
        // 운영진 여부 판단
        Boolean isStaff = loginUser.getPlayerRole() == ClubPlayerRole.OWNER
                || loginUser.getPlayerRole() == ClubPlayerRole.COACH;
        // Dto 반환
        return matchMapper.toMatchDto(match, isStaff);
    }

    private void checkClubIsExist(UUID clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
    }
}
