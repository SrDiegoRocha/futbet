package com.example.futbet.dto.request;

import com.example.futbet.enums.MatchType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;
import java.util.UUID;

public record CreateMatchRequest(
        @NotNull UUID homeTeamId,
        @NotNull UUID awayTeamId,
        @NotNull @PositiveOrZero Integer round,
        UUID groupId,
        UUID tieId,
        Instant scheduledAt,
        MatchType matchType   // opcional; default REGULAR. THIRD_PLACE só em KNOCKOUT.
) {
}
