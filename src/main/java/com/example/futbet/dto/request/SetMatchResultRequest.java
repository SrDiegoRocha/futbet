package com.example.futbet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record SetMatchResultRequest(
        @NotNull @PositiveOrZero Integer homeScore,
        @NotNull @PositiveOrZero Integer awayScore,
        // Pênaltis opcionais: só em mata-mata, ambos juntos e diferentes entre si.
        @PositiveOrZero Integer homePenalties,
        @PositiveOrZero Integer awayPenalties
) {
}
