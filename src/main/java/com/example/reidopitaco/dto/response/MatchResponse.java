package com.example.reidopitaco.dto.response;

import com.example.reidopitaco.enums.MatchStatus;
import com.example.reidopitaco.enums.MatchType;
import com.example.reidopitaco.enums.TeamType;

import java.time.Instant;
import java.util.UUID;

public record MatchResponse(
        UUID id,
        UUID phaseId,
        UUID groupId,
        String groupName,
        int round,
        UUID tieId,
        MatchType matchType,
        TeamRef homeTeam,
        TeamRef awayTeam,
        Instant scheduledAt,
        Integer homeScore,
        Integer awayScore,
        Integer homePenalties,
        Integer awayPenalties,
        MatchStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public record TeamRef(
            UUID id,
            String name,
            String shortName,
            String badgeUrl,
            String primaryColor,
            String secondaryColor,
            TeamType teamType,       // CLUB | NATIONAL_TEAM
            String countryCode       // flagicons (ex. "br"); preenchido nas seleções, senão null
    ) {
    }
}
