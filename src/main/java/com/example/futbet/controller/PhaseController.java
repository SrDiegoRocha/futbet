package com.example.futbet.controller;

import com.example.futbet.dto.request.CreatePhaseRequest;
import com.example.futbet.dto.request.MovePhaseRequest;
import com.example.futbet.dto.request.UpdatePhaseRequest;
import com.example.futbet.dto.response.PhaseResponse;
import com.example.futbet.service.PhaseService;
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
@RequestMapping("/api/tournaments/{tournamentId}/phases")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @PostMapping
    public ResponseEntity<PhaseResponse> create(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @Valid @RequestBody CreatePhaseRequest request
    ) {
        PhaseResponse response = phaseService.create(UUID.fromString(ownerPublicId), tournamentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PhaseResponse>> list(
            @AuthenticationPrincipal String requesterPublicId,
            @PathVariable UUID tournamentId
    ) {
        return ResponseEntity.ok(phaseService.list(UUID.fromString(requesterPublicId), tournamentId));
    }

    @GetMapping("/{phaseId}")
    public ResponseEntity<PhaseResponse> getById(
            @AuthenticationPrincipal String requesterPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId
    ) {
        return ResponseEntity.ok(phaseService.getById(UUID.fromString(requesterPublicId), tournamentId, phaseId));
    }

    @PutMapping("/{phaseId}")
    public ResponseEntity<PhaseResponse> update(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @Valid @RequestBody UpdatePhaseRequest request
    ) {
        return ResponseEntity.ok(
                phaseService.update(UUID.fromString(ownerPublicId), tournamentId, phaseId, request)
        );
    }

    @PostMapping("/{phaseId}/move")
    public ResponseEntity<PhaseResponse> move(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @Valid @RequestBody MovePhaseRequest request
    ) {
        return ResponseEntity.ok(
                phaseService.move(UUID.fromString(ownerPublicId), tournamentId, phaseId, request)
        );
    }

    @DeleteMapping("/{phaseId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId
    ) {
        phaseService.delete(UUID.fromString(ownerPublicId), tournamentId, phaseId);
        return ResponseEntity.noContent().build();
    }
}
