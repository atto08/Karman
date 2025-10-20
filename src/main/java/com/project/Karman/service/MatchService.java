package com.project.Karman.service;

import com.project.Karman.domain.entity.*;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.enums.Formation;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchGoalCreateRequestDto;
import com.project.Karman.dto.request.MatchLineupCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterCreateRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        Affiliation player = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 권한 체크 - USER 는 쿼터 생성불가
        if (player.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 클럽 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
        // 매치 조회
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MATCH));
        // 포메이션 변환
        Formation formation = Formation.fromName(requestDto.formation());
        if (formation.equals(Formation.NOT_VALID_FORMATION)) {
            throw new CustomException(ExceptionMessage.NOT_VALID_FORMATION);
        }
        // 쿼터 생성
        MatchQuarter matchQuarter = matchMapper.toMatchQuarterEntity(requestDto, match, formation);
        // 라인업 추가
        for (MatchLineupCreateRequestDto lineupDto : requestDto.lineup()) {
            MatchLineup playerInLineup = matchMapper.toMatchQuarterLineupEntity(matchQuarter, lineupDto);
            matchQuarter.addLineup(playerInLineup);
        }
        // 득점 추가
        for (MatchGoalCreateRequestDto goalDto : requestDto.goalsInfo()) {
            MatchGoal scoreInfo = matchMapper.toMatchQuarterGoalEntity(matchQuarter, goalDto);
            matchQuarter.addScoredGoal(scoreInfo);
        }
        // 득점 계산
        matchQuarter.countGoal();
        // 쿼터 추가
        match.addMatchQuarter(matchQuarter);
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
