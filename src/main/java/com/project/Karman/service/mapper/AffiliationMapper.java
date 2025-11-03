package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.dto.response.PlayerInfoListResponseDto;
import com.project.Karman.dto.response.PlayerInfoResponseDto;
import com.project.Karman.dto.response.PlayerSelectListResponseDto;
import com.project.Karman.dto.response.PlayerSelectResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AffiliationMapper {

    public Affiliation toAffiliationEntity(Member member, Club club, ClubPlayerRole playerRole) {

        return Affiliation.of(member, club, playerRole);
    }

    public PlayerSelectResponseDto toPlayerSelectDto(Affiliation affiliation) {

        return PlayerSelectResponseDto.of(
                affiliation.getAffiliationId(),
                affiliation.getMember().getName(),
                affiliation.getBackNumber(),
                affiliation.getPlayerPosition());
    }

    public PlayerSelectListResponseDto toPlayerSelectListDto(UUID clubId, List<Affiliation> affiliations) {

        List<PlayerSelectResponseDto> playerSelectResponseDtoList = affiliations.stream()
                .map(this::toPlayerSelectDto)
                .toList();

        return PlayerSelectListResponseDto.of(
                clubId,
                playerSelectResponseDtoList);
    }

    public PlayerInfoResponseDto toPlayerInfoDto(Affiliation affiliation) {

        return PlayerInfoResponseDto.of(
                affiliation.getMember().getName(),
                affiliation.getBackNumber(),
                affiliation.getPlayerPosition(),
                affiliation.getMatchCount(),
                affiliation.getGoal(),
                affiliation.getAssist(),
                affiliation.getClear(),
                affiliation.getPoint(),
                affiliation.getPlayerRole().toString());
    }

    public PlayerInfoListResponseDto toPlayerInfoListDto(UUID clubId, List<Affiliation> affiliations) {

        List<PlayerInfoResponseDto> playerInfoResponseDtoList = affiliations.stream()
                .map(this::toPlayerInfoDto)
                .toList();

        return PlayerInfoListResponseDto.of(
                clubId,
                playerInfoResponseDtoList);
    }
}
