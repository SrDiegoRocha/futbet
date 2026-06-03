package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.ZoneResponse;
import com.example.reidopitaco.entity.TournamentPhase;
import com.example.reidopitaco.entity.TournamentZone;
import org.springframework.stereotype.Component;

@Component
public class ZoneMapper {

    public ZoneResponse toResponse(TournamentZone zone) {
        TournamentPhase next = zone.getNextPhase();
        return new ZoneResponse(
                zone.getPublicId(),
                zone.getName(),
                zone.getFromPosition(),
                zone.getToPosition(),
                zone.getSelectionMode(),
                zone.getBestRankedCount(),
                next != null ? next.getPublicId() : null,
                next != null ? next.getName() : null,
                zone.getPosition(),
                zone.getCreatedAt(),
                zone.getUpdatedAt()
        );
    }
}
