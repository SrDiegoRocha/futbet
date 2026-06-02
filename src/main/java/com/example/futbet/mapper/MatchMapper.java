package com.example.futbet.mapper;

import com.example.futbet.dto.response.MatchResponse;
import com.example.futbet.entity.Match;
import com.example.futbet.entity.PhaseGroup;
import com.example.futbet.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchResponse toResponse(Match match) {
        PhaseGroup group = match.getGroup();
        return new MatchResponse(
                match.getPublicId(),
                match.getPhase().getPublicId(),
                group != null ? group.getPublicId() : null,
                group != null ? group.getName() : null,
                match.getRound(),
                match.getTieId(),
                match.getMatchType(),
                toTeamRef(match.getHomeTeam()),
                toTeamRef(match.getAwayTeam()),
                match.getScheduledAt(),
                match.getHomeScore(),
                match.getAwayScore(),
                match.getHomePenalties(),
                match.getAwayPenalties(),
                match.getStatus(),
                match.getCreatedAt(),
                match.getUpdatedAt()
        );
    }

    public MatchResponse.TeamRef toTeamRef(Team team) {
        return new MatchResponse.TeamRef(
                team.getPublicId(),
                team.getName(),
                team.getShortName(),
                team.getBadgeUrl(),
                team.getPrimaryColor(),
                team.getSecondaryColor()
        );
    }
}
