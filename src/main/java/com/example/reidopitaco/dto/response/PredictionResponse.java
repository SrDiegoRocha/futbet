package com.example.reidopitaco.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PredictionResponse(
        UUID id,
        UUID matchId,
        UUID userId,
        String userName,
        Integer homeScore,
        Integer awayScore,
        Integer points,
        Instant createdAt,
        Instant updatedAt
) {
}
