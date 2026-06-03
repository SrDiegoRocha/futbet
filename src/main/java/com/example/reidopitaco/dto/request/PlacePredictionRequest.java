package com.example.reidopitaco.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PlacePredictionRequest(
        @NotNull @PositiveOrZero Integer homeScore,
        @NotNull @PositiveOrZero Integer awayScore
) {
}
