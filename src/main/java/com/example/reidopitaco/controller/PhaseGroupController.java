package com.example.reidopitaco.controller;

import com.example.reidopitaco.dto.request.CreatePhaseGroupRequest;
import com.example.reidopitaco.dto.request.UpdatePhaseGroupRequest;
import com.example.reidopitaco.dto.response.PhaseGroupResponse;
import com.example.reidopitaco.service.PhaseGroupService;
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
@RequestMapping("/api/tournaments/{tournamentId}/phases/{phaseId}/groups")
public class PhaseGroupController {

    private final PhaseGroupService groupService;

    public PhaseGroupController(PhaseGroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<PhaseGroupResponse> create(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @Valid @RequestBody CreatePhaseGroupRequest request
    ) {
        PhaseGroupResponse response = groupService.create(
                UUID.fromString(ownerPublicId), tournamentId, phaseId, request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PhaseGroupResponse>> list(
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId
    ) {
        return ResponseEntity.ok(groupService.list(tournamentId, phaseId));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<PhaseGroupResponse> update(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @PathVariable UUID groupId,
            @Valid @RequestBody UpdatePhaseGroupRequest request
    ) {
        return ResponseEntity.ok(
                groupService.update(UUID.fromString(ownerPublicId), tournamentId, phaseId, groupId, request)
        );
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal String ownerPublicId,
            @PathVariable UUID tournamentId,
            @PathVariable UUID phaseId,
            @PathVariable UUID groupId
    ) {
        groupService.delete(UUID.fromString(ownerPublicId), tournamentId, phaseId, groupId);
        return ResponseEntity.noContent().build();
    }
}
