package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Affiliation;
import com.project.Karman.dto.PlayersInfoResponse;
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
}
