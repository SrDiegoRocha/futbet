package com.example.futbet.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PhaseGroupResponse(
        UUID id,
        String name,
        int position,
        long teamCount,
        Instant createdAt,
        Instant updatedAt
) {
}
