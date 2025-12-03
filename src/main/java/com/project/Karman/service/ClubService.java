package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Match;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.*;
import com.project.Karman.dto.request.*;
import com.project.Karman.dto.response.*;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final AffiliationRepository affiliationRepository;
    private final AffiliationMapper affiliationMapper;
    private final ClubMapper clubMapper;


    @Transactional
    public void createClub(Member member, ClubCreateRequestDto requestDto) {
        // 유저 정보 영속성 컨텍스트
        Member user = memberRepository.findById(member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MEMBER));
        // 클럽 객체생성 및 저장
        Club club = clubMapper.toClubEntity(user, requestDto);
        // 연관관계 객체생성
        Affiliation owner = affiliationMapper.toAffiliationEntity(club, user, user.getName(), 0,
                ClubPlayerPosition.GK, ClubPlayerRole.OWNER, ClubJoinStatus.APPROVED);
        club.addPlayer(owner);
        clubRepository.save(club);
    }

    @Transactional(readOnly = true)
    public ClubInfoResponseDto getClubInfo(Member member, UUID clubId) {
        // 클럽 상세조회
        Club club = findClubById(clubId);
        // TODO - 클럽에 소속한 선수는 디테일 정보 열람가능하도록 수정

        return clubMapper.toClubInfoDto(club);
    }

    @Transactional
    public void modifyClubInfo(Member member, UUID clubId, ClubUpdateRequestDto requestDto) {
        // 클럽 체크
        Club club = findClubById(clubId);
        // 구단주와 접근 유저 동일 여부 체크
        if (!club.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }

        ClubAgeGroup updatedClubAgeGroup = requestDto.ageGroup() != null ? ClubAgeGroup.fromDescription(requestDto.ageGroup()) : null;
        // 수정된 내용 적용 - 더티체킹
        club.update(requestDto.clubName(), requestDto.area(), updatedClubAgeGroup, requestDto.foundationDate());
    }

    @Transactional
    public void deleteClub(Member member, UUID clubId) {
        // 클럽 체크
        Club club = findClubById(clubId);
        // 구단주와 접근 유저 동일 여부 체크
        if (!club.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 클럽 삭제.
        clubRepository.delete(club);
    }

    @Transactional(readOnly = true)
    public List<SearchClubResponseDto> searchClub(String param) {
        List<Club> searchClubs = clubRepository.findByClubNameContainingOrderByClubNameAsc(param);

        List<SearchClubResponseDto> searchClubInfos = new ArrayList<>();
        for (Club club : searchClubs) {
            searchClubInfos.add(new SearchClubResponseDto(club.getClubId(), club.getClubName()));
        }

        return searchClubInfos;
    }

    @Transactional(readOnly = true)
    public PlayerInfoListResponseDto getPlayerInfoList(Member member, UUID clubId) {

        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }

        List<Affiliation> affiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatus(clubId, ClubJoinStatus.APPROVED);

        return affiliationMapper.toPlayerInfoListDto(clubId, affiliations);
    }

    @Transactional(readOnly = true)
    public PlayerSelectListResponseDto findPlayerSelectList(UUID clubId) {
        // 클럽 존재 여부 확인
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 클럽에 속한 선수 목록 조회
        List<Affiliation> affiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatus(clubId, ClubJoinStatus.APPROVED);

        return affiliationMapper.toPlayerSelectListDto(clubId, affiliations);
    }

    @Transactional
    public void requestJoinClub(Member member, UUID clubId) {
        // 클럽 조회
        Club club = findClubById(clubId);
        // 유저 조회
        Optional<Affiliation> player = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId());
        if (player.isPresent()) {
            throw new CustomException(ExceptionMessage.ALREADY_JOINED_PLAYER);
        }
        // 연관관계 객체생성
        Member user = memberRepository.findById(member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_MEMBER));
        // 가입 신청
        Affiliation requestJoinMember = affiliationMapper.toAffiliationEntity(club, user, user.getName(), 0,
                ClubPlayerPosition.GK, ClubPlayerRole.USER, ClubJoinStatus.PENDING);
        club.addPlayer(requestJoinMember);
    }

    @Transactional
    public String updateClubJoinStatus(Member member, UUID clubId, UUID affiliationId, JoinStatusUpdateRequestDto requestDto) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 소속팀에서 로그인 유저의 권한 확인
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 가입 요청한 선수 정보
        Affiliation player = affiliationRepository.findById(affiliationId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 가입 상태 수정
        player.updateJoinStatus(requestDto.joinStatus());
        // 여부에 따라서 메시지 변경
        return requestDto.joinStatus().equals(ClubJoinStatus.APPROVED) ? "가입 승인 처리 완료" : "가입 철회 처리 완료";
    }

    @Transactional
    public void withdrawClub(Member member, UUID clubId) {
        // 클럽 조회
        Club club = findClubById(clubId);
        if (club.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ExceptionMessage.OWNER_CAN_NOT_WITHDRAW_CLUB);
        }
        // 선수 조회
        Affiliation player = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 삭제
        affiliationRepository.delete(player);
    }

    @Transactional
    public void updatePlayerInfo(Member member, UUID clubId, UUID playerMemberId, PlayerStatUpdateRequestDto requestDto) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 소속팀에서 로그인 유저의 권한 확인
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // TODO - 구단주 권한으로 수정 요청 -> 체계적인 수정 필요
        // 구단주로 변경 요청은 거부
        if (requestDto.playerRole() != null) {
            if (requestDto.playerRole().equals(ClubPlayerRole.OWNER)) {
                throw new CustomException(ExceptionMessage.NOT_ALLOWED_OWNER_ROLE);
            }
        }

        // 가입 요청한 선수 정보
        Affiliation player = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, playerMemberId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 선수 정보수정
        player.updatePlayerInfo(requestDto.position(), requestDto.backNumber(), requestDto.playerRole());
    }

    @Transactional(readOnly = true)
    public ClubStaticsRecordsResponseDto getStaticsRecords(Member member, UUID clubId) {
        // 클럽 조회
        Club club = findClubById(clubId);
        // 수치 계산
        Long matchCount = (long) club.getMatches().size();
        Long win = 0L, draw = 0L, lose = 0L;
        Long scoreGoals = 0L, concedeGoals = 0L;
        for (Match match : club.getMatches()) {
            if (match.getMatchResult().equals(MatchResult.WIN)) {
                win++;
            } else if (match.getMatchResult().equals(MatchResult.DRAW)) {
                draw++;
            } else {
                lose++;
            }
            scoreGoals += match.getScoredGoal();
            concedeGoals += match.getConcededGoal();
        }

        return clubMapper.toClubStaticsRecordsDto(matchCount, win, draw, lose, scoreGoals, concedeGoals);
    }

    @Transactional
    public void addPlayerWithoutMember(Member member, UUID clubId, PlayerCreateRequestDto requestDto) {
        // Club 조회
        Club club = findClubById(clubId);
        // 유저 권한 조회
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 선수 객체 생성
        Affiliation affiliation = affiliationMapper.toAffiliationEntity(club, null, requestDto.playerName(), requestDto.backNumber(),
                requestDto.position(), ClubPlayerRole.USER, ClubJoinStatus.APPROVED);
        // 선수 추가
        club.addPlayer(affiliation);
    }

    @Transactional(readOnly = true)
    public MyClubListResponseDto getMyClubList(Member member) {

        List<Club> clubs = clubRepository.findClubsByMemberBelongsTo(member.getMemberId());

        return clubMapper.toMyClubListDto(clubs);
    }

    @Transactional(readOnly = true)
    public ClubJoinRequestListResponseDto getClubJoinRequests(Member member, UUID clubId) {
        // 클럽 조회
        if(!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 권한 체크
        validateUserClubRoleIsManagement(clubId, member.getMemberId());
        // 가입요청 보낸 선수 목록
        List<Affiliation> clubJoinRequestAffiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatus(clubId, ClubJoinStatus.PENDING);
        return affiliationMapper.toClubJoinRequestListDto(clubId, clubJoinRequestAffiliations);
    }

    // 클럽 상세조회
    private Club findClubById(UUID clubId) {
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
