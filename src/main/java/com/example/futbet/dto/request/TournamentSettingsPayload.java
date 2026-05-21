package com.example.futbet.dto.request;

import com.example.futbet.enums.TiebreakCriteria;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record TournamentSettingsPayload(
        @NotNull @PositiveOrZero Integer winPoints,
        @NotNull @PositiveOrZero Integer drawPoints,
        @NotNull @PositiveOrZero Integer lossPoints,

        @NotNull @PositiveOrZero Integer exactScorePoints,
        @NotNull @PositiveOrZero Integer winnerPoints,
        @NotNull @PositiveOrZero Integer wrongPoints,

        @NotEmpty List<TiebreakCriteria> tiebreakCriteria
) {
}
