package com.example.futbet.mapper;

import com.example.futbet.dto.response.PhaseTeamResponse;
import com.example.futbet.entity.PhaseGroup;
import com.example.futbet.entity.PhaseTeam;
import com.example.futbet.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class PhaseTeamMapper {

    public PhaseTeamResponse toResponse(PhaseTeam phaseTeam) {
        Team team = phaseTeam.getTeam();
        PhaseGroup group = phaseTeam.getGroup();
        return new PhaseTeamResponse(
                team.getPublicId(),
                team.getName(),
                team.getShortName(),
                team.getBadgeUrl(),
                team.getPrimaryColor(),
                team.getSecondaryColor(),
                group != null ? group.getPublicId() : null,
                group != null ? group.getName() : null,
                phaseTeam.getAddedAt()
        );
    }
}
