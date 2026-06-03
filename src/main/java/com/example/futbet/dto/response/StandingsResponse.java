package com.example.futbet.dto.response;

import com.example.futbet.enums.TeamType;

import java.util.List;
import java.util.UUID;

public record StandingsResponse(
        UUID phaseId,
        List<GroupStandings> groups
) {
    public record GroupStandings(
            UUID groupId,
            String groupName,
            List<StandingRow> rows
    ) {
    }

    public record StandingRow(
            int position,
            UUID teamId,
            String teamName,
            String shortName,
            String badgeUrl,
            TeamType teamType,
            String countryCode,
            int played,
            int wins,
            int draws,
            int losses,
            int goalsFor,
            int goalsAgainst,
            int goalDifference,
            int points,
            // Desfecho da zona de avanço para esta posição (null se nenhuma zona cobre a posição).
            UUID zoneId,
            String zoneName,
            UUID nextPhaseId,       // destino do avanço; null = zona de descarte/eliminação
            String nextPhaseName,
            boolean qualifies       // true se o time avança (resolve BEST_RANKED de forma autoritativa)
    ) {
    }
}
