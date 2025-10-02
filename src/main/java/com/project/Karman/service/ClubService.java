package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.AgeGroup;
import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.dto.*;
import com.project.Karman.service.mapper.AffiliationMapper;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import com.project.Karman.service.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMapper affiliationMapper;
    private final ClubMapper clubMapper;

    @Transactional(readOnly = true)
    public List<PlayersInfoResponse> findPlayersInfoByClub(UUID clubId) {
        // 클럽 존재 여부 확인
        Club club = findClub(clubId);
        // 클럽에 속한 선수 목록 조회
        List<Affiliation> affiliations = affiliationRepository.findAllByClub(club);
        // entity -> dto
        List<PlayersInfoResponse> playersInfo = new ArrayList<>();
        for (Affiliation player : affiliations) {
            PlayersInfoResponse info = affiliationMapper.toDto(player);
            playersInfo.add(info);
        }

        return playersInfo;
    }


    @Transactional
    public void createClub(Member member, ClubCreateRequestDto request) {
        // 클럽 객체생성 및 저장
        Club club = clubMapper.toEntity(member.getMemberId(), request);
        Affiliation headCoach = affiliationMapper.toEntity(club, member);
        clubRepository.save(club);
        affiliationRepository.save(headCoach);
    }

    @Transactional
    public void modifyClubInfo(UUID clubId, Member member, ClubUpdateRequestDto request) {
        // 클럽 체크
        Club club = findClub(clubId);
        // 구단주와 접근 유저 동일 여부 체크
        if (!club.getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("권한이 없는 유저입니다.");
        }

        AgeGroup updatedAgeGroup = request.ageGroup() != null ? AgeGroup.fromDescription(request.ageGroup()) : null;
        // 수정된 내용 적용 - 더티체킹
        club.update(request.clubName(), request.area(), updatedAgeGroup, request.foundationDate());
    }

    @Transactional
    public void deleteClub(UUID clubId, Member member) {
        // 클럽 체크
        Club club = findClub(clubId);
        // 구단주와 접근 유저 동일 여부 체크
        if (!club.getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("권한이 없는 유저입니다.");
        }
        // 클럽 삭제.
        clubRepository.delete(club);
    }

    @Transactional(readOnly = true)
    public List<SearchClubResponseDto> searchClub(String param) {
        List<Club> searchClubs = clubRepository.findAllClubs(param);

        List<SearchClubResponseDto> searchClubInfos = new ArrayList<>();
        for (Club club : searchClubs) {
            searchClubInfos.add(new SearchClubResponseDto(club.getClubId(), club.getClubName()));
        }

        return searchClubInfos;
    }

    @Transactional(readOnly = true)
    public ClubInfoResponseDto getClubInfo(UUID clubId, Member member) {
        // 클럽 상세조회
        Club club = findClub(clubId);
        // TODO - 클럽에 소속한 선수는 디테일 정보 열람가능하도록 수정

        return clubMapper.toDto(club);
    }

    @Transactional
    public void requestJoinClub(UUID clubId, Member member) {
        // 클럽 조회
        Club club = findClub(clubId);
        // 유저 조회
        Optional<Affiliation> player = affiliationRepository.findByClubIdAndMemberId(clubId, member.getMemberId());
        if (player.isPresent()) {
            throw new IllegalArgumentException("이미 가입된 선수 입니다.");
        }
        // 가입 신청
        Affiliation requestJoinMember = affiliationMapper.toEntity(club, member);
        affiliationRepository.save(requestJoinMember);
    }

    @Transactional
    public String updateClubJoinStatus(UUID clubId, UUID playerId, JoinStatusUpdateRequestDto request, Member member) {
        // 클럽 조회
        Club club = findClub(clubId);
        // 로그인 유저 권한 체크
        if (!club.getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("구단주가 아닌 유저는 가입 처리를 진행할 수 없습니다.");
        }
        // 가입 요청한 선수 정보
        Affiliation player = affiliationRepository.findByClubIdAndMemberId(clubId, playerId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 선수입니다."));
        // 가입 상태 수정
        player.updateJoinStatus(request.joinStatus());
        // 여부에 따라서 메시지 변경
        return request.joinStatus().equals(ClubJoinStatus.APPROVED) ? "가입 승인 처리 완료" : "가입 철회 처리 완료";
    }

    //
    private Club findClub(UUID clubId) {
        // 클럽 상세조회
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 클럽입니다."));
    }
}
