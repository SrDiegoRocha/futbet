package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.TeamResponse;
import com.example.reidopitaco.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getPublicId(),
                team.getName(),
                team.getShortName(),
                team.getBadgeUrl(),
                team.getPrimaryColor(),
                team.getSecondaryColor(),
                team.isSystem(),
                team.getTeamType(),
                team.getCountryCode(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}
