package com.example.futbet.dto.response;

import com.example.futbet.enums.MatchGenerationMode;
import com.example.futbet.enums.MatchLegMode;
import com.example.futbet.enums.TournamentPhaseType;

import java.time.Instant;
import java.util.UUID;

public record PhaseResponse(
        UUID id,
        String name,
        int position,
        TournamentPhaseType phaseType,
        MatchLegMode matchLegMode,
        MatchGenerationMode matchGenerationMode,
        Boolean playsInsideGroupOnly,
        boolean hasThirdPlace,
        long groupCount,
        long teamCount,
        Instant finalizedAt,
        Instant createdAt,
        Instant updatedAt
) {
}
