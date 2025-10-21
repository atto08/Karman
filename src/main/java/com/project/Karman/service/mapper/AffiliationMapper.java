package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.domain.enums.ClubPlayerRole;
import com.project.Karman.dto.response.PlayersInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class AffiliationMapper {

    public Affiliation toEntity(Member member, Club club, ClubPlayerRole playerRole) {

        return Affiliation.of(member, club, playerRole);
    }


    public PlayersInfoResponse toDto(Affiliation affiliation) {

        return PlayersInfoResponse.of(
                affiliation.getMember().getMemberId(),
                affiliation.getMember().getName(),
                affiliation.getBackNumber(),
                affiliation.getPosition());
    }
}
