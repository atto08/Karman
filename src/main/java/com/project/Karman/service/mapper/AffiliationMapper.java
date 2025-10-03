package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.domain.entity.Club;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.dto.response.PlayersInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class AffiliationMapper {

    public PlayersInfoResponse toDto(Affiliation affiliation) {

        return PlayersInfoResponse.of(
                affiliation.getMember().getMemberId(),
                affiliation.getMember().getName(),
                affiliation.getBackNumber(),
                affiliation.getPosition());
    }


    public Affiliation toEntity(Club club, Member member) {

        return Affiliation.of(member, club);
    }
}
