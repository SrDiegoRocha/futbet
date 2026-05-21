package com.example.futbet.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PhaseTeamResponse(
        UUID teamId,
        String teamName,
        String shortName,
        String badgeUrl,
        String primaryColor,
        String secondaryColor,
        UUID groupId,
        String groupName,
        Instant addedAt
) {
}
