package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubJoinStatus;
import com.project.Karman.domain.enums.ClubPlayerPosition;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.domain.vo.PlayerStatsDelta;
import com.project.Karman.dto.batch.PlayerStatBatchDto;
import com.project.Karman.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AffiliationMapper {

    public Affiliation toAffiliationEntity(Club club, Member member, String playerName, Integer backNumber,
                                           ClubPlayerPosition playerPosition, ClubPlayerRole playerRole, ClubJoinStatus joinStatus) {

        return Affiliation.of(
                club,
                member,
                playerName,
                backNumber,
                playerPosition,
                playerRole,
                joinStatus);
    }

    public PlayerSelectResponseDto toPlayerSelectDto(Affiliation affiliation) {

        return PlayerSelectResponseDto.of(
                affiliation.getAffiliationId(),
                affiliation.getPlayerName(),
                affiliation.getBackNumber(),
                affiliation.getPlayerPosition()
        );
    }

    public PlayerSelectListResponseDto toPlayerSelectListDto(UUID clubId, List<Affiliation> affiliations) {

        List<PlayerSelectResponseDto> playerSelectResponseDtoList = affiliations.stream()
                .map(this::toPlayerSelectDto)
                .toList();

        return PlayerSelectListResponseDto.of(
                clubId,
                playerSelectResponseDtoList
        );
    }

    public PlayerInfoResponseDto toPlayerInfoDto(Affiliation affiliation) {

        return PlayerInfoResponseDto.of(
                affiliation.getAffiliationId(),
                affiliation.getPlayerName(),
                affiliation.getBackNumber(),
                affiliation.getPlayerPosition(),
                affiliation.getMatchCount(),
                affiliation.getGoal(),
                affiliation.getAssist(),
                affiliation.getClear(),
                affiliation.getPoint(),
                affiliation.getPlayerRole().toString());
    }

    public PlayerInfoListResponseDto toPlayerInfoListDto(UUID clubId, Boolean isStaff, List<Affiliation> affiliations) {

        List<PlayerInfoResponseDto> playerInfoResponseDtoList = affiliations.stream()
                .map(this::toPlayerInfoDto)
                .toList();

        return PlayerInfoListResponseDto.of(
                clubId,
                isStaff,
                playerInfoResponseDtoList
        );
    }

    public PlayerStatBatchDto toPlayerStatBatchDto(UUID affiliationId, PlayerStatsDelta delta) {

        return PlayerStatBatchDto.of(
                affiliationId,
                delta.getMatchCount(),
                delta.getGoal(),
                delta.getAssist()
        );
    }

    public ClubJoinRequestResponseDto toClubJoinRequestDto(Affiliation affiliation) {

        return ClubJoinRequestResponseDto.of(
                affiliation.getAffiliationId(),
                affiliation.getPlayerName()
        );
    }

    public ClubJoinRequestListResponseDto toClubJoinRequestListDto(UUID clubId, List<Affiliation> affiliations) {

        List<ClubJoinRequestResponseDto> clubJoinRequestResponseDtoList = affiliations.stream()
                .map(this::toClubJoinRequestDto)
                .toList();

        return ClubJoinRequestListResponseDto.of(
                clubId,
                clubJoinRequestResponseDtoList
        );
    }
}
