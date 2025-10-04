package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Match;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.MatchRepository;
import com.project.Karman.service.mapper.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final AffiliationRepository affiliationRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public void createMatch(MatchCreateRequestDto request, UUID clubId, Member member) {
        // 클럽 소속 선수 여부
        Affiliation player = affiliationRepository.findByClubIdAndMemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("클럽에 소속되지 않은 선수입니다."));
        // 권한 체크
        if (player.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new IllegalArgumentException("운영진이 아닌 회원은 매치를 등록할 수 없습니다.");
        }
        // 매치 객체 생성 & 저장
        Match match = matchMapper.toEntity(request, clubId);
        matchRepository.save(match);
    }
}
