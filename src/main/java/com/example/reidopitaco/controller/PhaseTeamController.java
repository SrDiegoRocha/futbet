package com.example.reidopitaco.controller;

import com.example.reidopitaco.dto.request.MovePhaseTeamRequest;
import com.example.reidopitaco.dto.response.PhaseTeamResponse;
import com.example.reidopitaco.service.PhaseTeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments/{tournamentId}/phases/{phaseId}/teams")
public class PhaseTeamController {

    private final PhaseTeamService phaseTeamService;

    public PhaseTeamController(PhaseTeamService phaseTeamService) {
        this.phaseTeamService = phaseTeamService;
    }

    @GetMapping
    public ResponseEntity<List<PhaseTeamResponse>> list(
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId
    ) {
        return ResponseEntity.ok(phaseTeamService.list(tournamentId, phaseId));
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<PhaseTeamResponse> add(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @PathVariable UUID teamId
    ) {
        PhaseTeamResponse response = phaseTeamService.add(
                UUID.fromString(ownerPublicId), tournamentId, phaseId, teamId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<PhaseTeamResponse> move(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @PathVariable UUID teamId,
            @Valid @RequestBody MovePhaseTeamRequest request
    ) {
        return ResponseEntity.ok(
                phaseTeamService.move(UUID.fromString(ownerPublicId), tournamentId, phaseId, teamId, request)
        );
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> remove(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @PathVariable UUID teamId
    ) {
        phaseTeamService.remove(UUID.fromString(ownerPublicId), tournamentId, phaseId, teamId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/draw")
    public ResponseEntity<List<PhaseTeamResponse>> draw(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId
    ) {
        return ResponseEntity.ok(
                phaseTeamService.draw(UUID.fromString(ownerPublicId), tournamentId, phaseId)
        );
    }
}
