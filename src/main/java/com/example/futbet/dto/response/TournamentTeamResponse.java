package com.example.futbet.dto.response;

import com.example.futbet.enums.TeamType;

import java.time.Instant;
import java.util.UUID;

public record TournamentTeamResponse(
        UUID teamId,
        String name,
        String shortName,
        String badgeUrl,
        String primaryColor,
        String secondaryColor,
        boolean system,
        TeamType teamType,
        String countryCode,
        Instant addedAt
) {
}
