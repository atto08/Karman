package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.dto.CreateClubRequest;
import com.project.Karman.repository.MemberRepository;
import com.project.Karman.service.mapper.AffiliationMapper;
import com.project.Karman.dto.PlayersInfoResponse;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
import com.project.Karman.service.mapper.ClubMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final AffiliationRepository affiliationRepository;
    // TODO: 회원 기능 구현 후 제거
    private final MemberRepository memberRepository;
    private final AffiliationMapper affiliationMapper;
    private final ClubMapper clubMapper;

    @Transactional(readOnly = true)
    public List<PlayersInfoResponse> findPlayersInfoByClub(UUID clubId) {
        // 클럽 존재 여부 확인
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 클럽입니다."));
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


    // TODO: 로그인 기능 구현 후 request에서 memeber 제거
    @Transactional
    public void createClub(CreateClubRequest request) {
        // 회원 체크
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Club club = clubMapper.toEntity(request);
        clubRepository.save(club);
    }

    @Transactional
    public void deleteClub(UUID clubId, UUID memberId) {
        // 회원 체크
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 클럽 체크
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 클럽입니다."));
        // 구단주와 접근 유저 동일 여부 체크
        if (!club.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("권한이 없는 유저입니다.");
        }
        // 클럽 삭제.
        clubRepository.delete(club);
    }
}
