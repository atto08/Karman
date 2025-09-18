package com.project.Karman.service;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.mapper.AffiliationMapper;
import com.project.Karman.dto.PlayersInfoResponse;
import com.project.Karman.repository.AffiliationRepository;
import com.project.Karman.repository.ClubRepository;
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
    private final AffiliationMapper affiliationMapper;

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
}
