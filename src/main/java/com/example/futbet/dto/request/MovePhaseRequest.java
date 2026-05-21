package com.example.futbet.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MovePhaseRequest(
        @NotNull @PositiveOrZero Integer position
) {
}
