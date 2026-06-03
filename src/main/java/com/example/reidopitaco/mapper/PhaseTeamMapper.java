package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.PhaseTeamResponse;
import com.example.reidopitaco.entity.PhaseGroup;
import com.example.reidopitaco.entity.PhaseTeam;
import com.example.reidopitaco.entity.Team;
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
                team.getTeamType(),
                team.getCountryCode(),
                group != null ? group.getPublicId() : null,
                group != null ? group.getName() : null,
                phaseTeam.getAddedAt()
        );
    }
}
