package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.TournamentTeamResponse;
import com.example.reidopitaco.entity.Team;
import com.example.reidopitaco.entity.TournamentTeam;
import org.springframework.stereotype.Component;

@Component
public class TournamentTeamMapper {

    public TournamentTeamResponse toResponse(TournamentTeam tournamentTeam) {
        Team team = tournamentTeam.getTeam();
        return new TournamentTeamResponse(
                team.getPublicId(),
                team.getName(),
                team.getShortName(),
                team.getBadgeUrl(),
                team.getPrimaryColor(),
                team.getSecondaryColor(),
                team.isSystem(),
                team.getTeamType(),
                team.getCountryCode(),
                tournamentTeam.getAddedAt()
        );
    }
}
