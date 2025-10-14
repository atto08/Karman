package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Match;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.response.MatchListResponseDto;
import com.project.Karman.exception.CustomException;
import com.project.Karman.exception.ExceptionMessage;
import com.project.Karman.repository.AffiliationRepository;
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

    private final MatchRepository matchRepository;
    private final AffiliationRepository affiliationRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public void createMatch(MatchCreateRequestDto request, UUID clubId, Member member) {
        // 클럽 소속 선수 여부
        Affiliation player = affiliationRepository.findByClubIdAndMemberId(clubId, member.getMemberId())
                .orElseThrow(() -> new CustomException(ExceptionMessage.NOT_FOUND_PLAYER_IN_CLUB));
        // 권한 체크
        if (player.getPlayerRole().equals(ClubPlayerRole.USER)) {
            throw new CustomException(ExceptionMessage.PERMISSION_DENIED_MEMBER);
        }
        // 매치 객체 생성 & 저장
        Match match = matchMapper.toEntity(request, clubId);
        matchRepository.save(match);
    }

    @Transactional(readOnly = true)
    public List<MatchListResponseDto> getMatchAll(Member member, UUID clubId) {

        // 클럽 아이디를 갖고 있는 경기 기록 전부 조회
        List<Match> matchList = matchRepository.findAllByClubId(clubId);
        // dto로 변환
        List<MatchListResponseDto> matchAllDto = new ArrayList<>();
        for(Match match : matchList) {
            MatchListResponseDto matchDto = matchMapper.toDto(match);
            matchAllDto.add(matchDto);
        }

        return matchAllDto;
    }
}
