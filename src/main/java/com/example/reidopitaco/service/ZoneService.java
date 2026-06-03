package com.example.reidopitaco.service;

import com.example.reidopitaco.dto.request.CreateZoneRequest;
import com.example.reidopitaco.dto.request.UpdateZoneRequest;
import com.example.reidopitaco.dto.response.ZoneResponse;
import com.example.reidopitaco.entity.PhaseGroup;
import com.example.reidopitaco.entity.Tournament;
import com.example.reidopitaco.entity.TournamentPhase;
import com.example.reidopitaco.entity.TournamentZone;
import com.example.reidopitaco.enums.TournamentPhaseType;
import com.example.reidopitaco.enums.ZoneSelectionMode;
import com.example.reidopitaco.exception.InvalidZoneException;
import com.example.reidopitaco.exception.PhaseNotFoundException;
import com.example.reidopitaco.exception.ZoneNotFoundException;
import com.example.reidopitaco.mapper.ZoneMapper;
import com.example.reidopitaco.repository.PhaseGroupRepository;
import com.example.reidopitaco.repository.TournamentPhaseRepository;
import com.example.reidopitaco.repository.TournamentZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ZoneService {

    private final PhaseService phaseService;
    private final TournamentPhaseRepository phaseRepository;
    private final TournamentZoneRepository zoneRepository;
    private final PhaseGroupRepository groupRepository;
    private final ZoneMapper mapper;

    public ZoneService(
            PhaseService phaseService,
            TournamentPhaseRepository phaseRepository,
            TournamentZoneRepository zoneRepository,
            PhaseGroupRepository groupRepository,
            ZoneMapper mapper
    ) {
        this.phaseService = phaseService;
        this.phaseRepository = phaseRepository;
        this.zoneRepository = zoneRepository;
        this.groupRepository = groupRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ZoneResponse create(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            CreateZoneRequest request
    ) {
        Tournament tournament = phaseService.loadOwnedNotFinished(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);

        validateZonePayload(
                phase,
                request.fromPosition(),
                request.toPosition(),
                request.selectionMode(),
                request.bestRankedCount(),
                null
        );

        TournamentPhase nextPhase = resolveNextPhase(tournament, phase, request.nextPhaseId());

        int position = (int) zoneRepository.countByPhaseId(phase.getId());
        TournamentZone zone = TournamentZone.builder()
                .phase(phase)
                .name(request.name().trim())
                .fromPosition(request.fromPosition())
                .toPosition(request.toPosition())
                .selectionMode(request.selectionMode())
                .bestRankedCount(
                        request.selectionMode() == ZoneSelectionMode.BEST_RANKED
                                ? request.bestRankedCount()
                                : null
                )
                .nextPhase(nextPhase)
                .position(position)
                .build();
        return mapper.toResponse(zoneRepository.save(zone));
    }

    @Transactional(readOnly = true)
    public List<ZoneResponse> list(UUID phasePublicId) {
        return zoneRepository.findAllByPhasePublicIdOrderByPositionAsc(phasePublicId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ZoneResponse update(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID zonePublicId,
            UpdateZoneRequest request
    ) {
        Tournament tournament = phaseService.loadOwnedNotFinished(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);
        TournamentZone zone = zoneRepository.findByPublicIdAndPhasePublicId(zonePublicId, phasePublicId)
                .orElseThrow(ZoneNotFoundException::new);

        validateZonePayload(
                phase,
                request.fromPosition(),
                request.toPosition(),
                request.selectionMode(),
                request.bestRankedCount(),
                zone.getId()
        );

        TournamentPhase nextPhase = resolveNextPhase(tournament, phase, request.nextPhaseId());

        zone.setName(request.name().trim());
        zone.setFromPosition(request.fromPosition());
        zone.setToPosition(request.toPosition());
        zone.setSelectionMode(request.selectionMode());
        zone.setBestRankedCount(
                request.selectionMode() == ZoneSelectionMode.BEST_RANKED
                        ? request.bestRankedCount()
                        : null
        );
        zone.setNextPhase(nextPhase);
        return mapper.toResponse(zoneRepository.saveAndFlush(zone));
    }

    @Transactional
    public void delete(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID zonePublicId
    ) {
        Tournament tournament = phaseService.loadOwnedNotFinished(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);
        TournamentZone zone = zoneRepository.findByPublicIdAndPhasePublicId(zonePublicId, phasePublicId)
                .orElseThrow(ZoneNotFoundException::new);
        zoneRepository.delete(zone);
    }

    private void validateZonePayload(
            TournamentPhase phase,
            int fromPosition,
            int toPosition,
            ZoneSelectionMode mode,
            Integer bestRankedCount,
            Long excludeZoneId
    ) {
        if (fromPosition > toPosition) {
            throw new InvalidZoneException("fromPosition must be <= toPosition");
        }
        if (mode == ZoneSelectionMode.BEST_RANKED) {
            if (phase.getPhaseType() != TournamentPhaseType.GROUPS) {
                throw new InvalidZoneException("BEST_RANKED zones are only valid on GROUPS phases");
            }
            if (fromPosition != toPosition) {
                throw new InvalidZoneException("BEST_RANKED zones require fromPosition == toPosition");
            }
            if (bestRankedCount == null || bestRankedCount <= 0) {
                throw new InvalidZoneException("bestRankedCount must be > 0 for BEST_RANKED zones");
            }
            long groupCount = groupRepository.countByPhaseId(phase.getId());
            if (groupCount > 0 && bestRankedCount > groupCount) {
                throw new InvalidZoneException(
                        "bestRankedCount cannot exceed number of groups (" + groupCount + ")"
                );
            }
        }

        List<TournamentZone> existing = zoneRepository.findAllByPhaseIdOrderByPositionAsc(phase.getId());
        for (TournamentZone other : existing) {
            if (excludeZoneId != null && other.getId().equals(excludeZoneId)) {
                continue;
            }
            boolean overlaps = !(toPosition < other.getFromPosition() || fromPosition > other.getToPosition());
            if (overlaps) {
                throw new InvalidZoneException(
                        "zone overlaps with existing zone '" + other.getName() + "'"
                );
            }
        }
    }

    private TournamentPhase resolveNextPhase(
            Tournament tournament,
            TournamentPhase currentPhase,
            UUID nextPhaseId
    ) {
        if (nextPhaseId == null) {
            return null;
        }
        TournamentPhase next = phaseRepository
                .findByPublicIdAndTournamentPublicId(nextPhaseId, tournament.getPublicId())
                .orElseThrow(PhaseNotFoundException::new);
        if (next.getPosition() <= currentPhase.getPosition()) {
            throw new InvalidZoneException("nextPhase must come after the current phase");
        }
        return next;
    }
}
