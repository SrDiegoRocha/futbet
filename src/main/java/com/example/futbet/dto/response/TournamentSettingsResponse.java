package com.example.futbet.dto.response;

import com.example.futbet.enums.TiebreakCriteria;

import java.util.List;

public record TournamentSettingsResponse(
        int winPoints,
        int drawPoints,
        int lossPoints,
        int exactScorePoints,
        int winnerPoints,
        int wrongPoints,
        List<TiebreakCriteria> tiebreakCriteria
) {
}
