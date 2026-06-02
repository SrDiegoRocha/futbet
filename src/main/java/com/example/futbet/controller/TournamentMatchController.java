package com.example.futbet.controller;

import com.example.futbet.dto.response.MatchResponse;
import com.example.futbet.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/matches")
public class TournamentMatchController {

    private final MatchService matchService;

    public TournamentMatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public ResponseEntity<List<MatchResponse>> listAll(
            @AuthenticationPrincipal String requesterPublicId,
            @PathVariable UUID tournamentId
    ) {
        return ResponseEntity.ok(
                matchService.listByTournament(UUID.fromString(requesterPublicId), tournamentId)
        );
    }
}
