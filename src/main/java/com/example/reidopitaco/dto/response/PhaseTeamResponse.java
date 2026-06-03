package com.example.reidopitaco.dto.response;

import com.example.reidopitaco.enums.TeamType;

import java.time.Instant;
import java.util.UUID;

public record PhaseTeamResponse(
        UUID teamId,
        String teamName,
        String shortName,
        String badgeUrl,
        String primaryColor,
        String secondaryColor,
        TeamType teamType,
        String countryCode,
        UUID groupId,
        String groupName,
        Instant addedAt
) {
}
