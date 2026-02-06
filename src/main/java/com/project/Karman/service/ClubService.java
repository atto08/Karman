package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.*;
import com.project.Karman.dto.request.*;
import com.project.Karman.dto.response.*;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.MatchRepository;
import com.project.Karman.repository.MemberRepository;
import com.project.Karman.repository.projection.ClubMatchStatisticsProjection;
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
    private final MatchRepository matchRepository;
    private final AffiliationMapper affiliationMapper;
    private final ClubMapper clubMapper;


    @Transactional
    public void createClub(Member member, ClubCreateRequestDto requestDto) {
        // 프록시 객체 생성
        Member ownerMember = memberRepository.getReferenceById(member.getMemberId());
        // 클럽 객체생성 및 저장
        Club club = clubMapper.toClubEntity(ownerMember, requestDto);
        // 연관관계 객체생성
        Affiliation owner = affiliationMapper.toAffiliationEntity(club, ownerMember, member.getName(), 0,
                ClubPlayerPosition.GK, ClubPlayerRole.OWNER, ClubJoinStatus.APPROVED);
        club.addPlayer(owner);
        clubRepository.save(club);
    }

    @Transactional(readOnly = true)
    public ClubInfoResponseDto getClubInfo(Member member, UUID clubId) {
        // 클럽 상세조회
        Club club = findClubById(clubId);
        // 로그인 유저 조회 클럽 가입여부 판단
        Boolean isAssociated = affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatus(clubId, member.getMemberId(), ClubJoinStatus.APPROVED);

        return clubMapper.toClubInfoDto(club, isAssociated);
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
    public SearchClubListResponseDto searchClub(String param) {
        List<Club> searchClubs = clubRepository.findByClubNameContainingOrderByClubNameAsc(param);

        return clubMapper.toSearchClubListDto(searchClubs);
    }

    @Transactional(readOnly = true)
    public PlayerInfoListResponseDto getPlayerInfoList(Member member, UUID clubId) {
        // 클럽 존재여부 판단
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 로그인 유저 클럽 소속여부 판단
        if (!affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatus(
                clubId,
                member.getMemberId(),
                ClubJoinStatus.APPROVED)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_USER_GET_CLUB);
        }
        // 선수단 조회
        List<Affiliation> affiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatusOrderByBackNumberAsc(clubId, ClubJoinStatus.APPROVED);
        // 로그인 유저 운영진(Owner or Coach) 여부 판단
        Boolean isStaff = affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatusAndPlayerRoleIn(
                clubId,
                member.getMemberId(),
                ClubJoinStatus.APPROVED,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH));

        return affiliationMapper.toPlayerInfoListDto(clubId, isStaff, getAffiliationsSortedByPlayerRole(affiliations));
    }

    @Transactional(readOnly = true)
    public PlayerSelectListResponseDto findPlayerSelectList(UUID clubId) {
        // 클럽 존재 여부 확인
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 클럽에 속한 선수 목록 조회
        List<Affiliation> affiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatusOrderByBackNumberAsc(clubId, ClubJoinStatus.APPROVED);

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
        // 프록시 객체 생성
        Member joinRequestMember = memberRepository.getReferenceById(member.getMemberId());
        // 가입 신청
        Affiliation requestJoinMember = affiliationMapper.toAffiliationEntity(club, joinRequestMember, member.getName(), 0,
                ClubPlayerPosition.GK, ClubPlayerRole.USER, ClubJoinStatus.PENDING);
        club.addPlayer(requestJoinMember);
    }

    @Transactional
    public String updateClubJoinStatus(Member member, UUID clubId, UUID affiliationId, JoinStatusUpdateRequestDto requestDto) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 로그인 유저 권한 체크
        validateMemberIsManagement(clubId, member.getMemberId());
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
    public void updatePlayerInfo(Member member, UUID clubId, UUID affiliationId, PlayerInfoUpdateRequestDto requestDto) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 로그인 유저 권한 체크
        validateMemberIsManagement(clubId, member.getMemberId());
        // 타겟 선수
        Affiliation player = affiliationRepository.findById(affiliationId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 선수 정보수정
        player.updatePlayerInfo(requestDto.position(), requestDto.backNumber());
    }

    @Transactional(readOnly = true)
    public ClubStatisticsRecordsResponseDto getStaticsRecords(Member member, UUID clubId) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        ClubMatchStatisticsProjection statistics = matchRepository.findClubMatchStatisticsByClubId(clubId, MatchResult.WIN, MatchResult.DRAW, MatchResult.LOSE);

        return clubMapper.toClubStatisticsRecordsDto(statistics);
    }

    @Transactional
    public void addPlayerWithoutMember(Member member, UUID clubId, PlayerCreateRequestDto requestDto) {
        // Club 조회
        Club club = findClubById(clubId);
        // 로그인 유저 권한 체크
        validateMemberIsManagement(clubId, member.getMemberId());
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
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 로그인 유저 권한 체크
        validateMemberIsManagement(clubId, member.getMemberId());
        // 가입요청 보낸 선수 목록
        List<Affiliation> clubJoinRequestAffiliations = affiliationRepository.findAllByClub_ClubIdAndJoinStatusOrderByBackNumberAsc(clubId, ClubJoinStatus.PENDING);
        return affiliationMapper.toClubJoinRequestListDto(clubId, clubJoinRequestAffiliations);
    }

    @Transactional(readOnly = true)
    public ClubMembersListResponseDto getClubMembers(Member member, UUID clubId) {
        // 클럽 조회
        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }
        // 클럽 소속 멤버 조회
        List<Affiliation> clubMembers = affiliationRepository.findAllByClub_ClubIdAndMember_MemberIdIsNotNull(clubId);
        return affiliationMapper.toClubMemberListDto(clubMembers);
    }

    @Transactional
    public void transferPlayerRecord(Member member, UUID clubId, TransferPlayerRecordRequestDto requestDto) {

        if (!clubRepository.existsById(clubId)) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_CLUB);
        }

        validateMemberIsManagement(clubId, member.getMemberId());

        Affiliation targetAffiliation = affiliationRepository.findByClub_ClubIdAndMember_MemberId(clubId, requestDto.memberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));

        Affiliation sourceAffiliation = affiliationRepository.findById(requestDto.affiliationId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));

        Member targetMember = memberRepository.getReferenceById(requestDto.memberId());
        sourceAffiliation.transferAffiliationInfo(targetAffiliation.getPlayerName(), targetMember);
        sourceAffiliation.updatePlayerInfo(targetAffiliation.getPlayerPosition(), targetAffiliation.getBackNumber());
        affiliationRepository.delete(targetAffiliation);
    }

    // 클럽 상세조회
    private Club findClubById(UUID clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_CLUB));
    }

    private void validateMemberIsManagement(UUID clubId, UUID memberId) {
        if (!affiliationRepository.existsByClub_ClubIdAndMember_MemberIdAndJoinStatusAndPlayerRoleIn(
                clubId,
                memberId,
                ClubJoinStatus.APPROVED,
                List.of(ClubPlayerRole.OWNER, ClubPlayerRole.COACH))) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
    }

    private List<Affiliation> getAffiliationsSortedByPlayerRole(List<Affiliation> players) {

        return players.stream()
                .sorted(Comparator
                        .comparing((Affiliation a) -> a.getPlayerRole().ordinal())
                        .thenComparing(Affiliation::getBackNumber))
                .toList();
    }
}
