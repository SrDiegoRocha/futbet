package com.example.reidopitaco.dto.request;

import com.example.reidopitaco.enums.MatchType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;
import java.util.UUID;

public record UpdateMatchRequest(
        @NotNull UUID homeTeamId,
        @NotNull UUID awayTeamId,
        @NotNull @PositiveOrZero Integer round,
        UUID groupId,
        Instant scheduledAt,
        MatchType matchType   // opcional; default REGULAR. THIRD_PLACE só em KNOCKOUT.
) {
}
