package com.example.reidopitaco.service;

import com.example.reidopitaco.dto.request.CreatePhaseGroupRequest;
import com.example.reidopitaco.dto.request.UpdatePhaseGroupRequest;
import com.example.reidopitaco.dto.response.PhaseGroupResponse;
import com.example.reidopitaco.entity.PhaseGroup;
import com.example.reidopitaco.entity.Tournament;
import com.example.reidopitaco.entity.TournamentPhase;
import com.example.reidopitaco.enums.TournamentPhaseType;
import com.example.reidopitaco.exception.GroupNameAlreadyInUseException;
import com.example.reidopitaco.exception.GroupOnlyAllowedInGroupsPhaseException;
import com.example.reidopitaco.exception.PhaseGroupNotFoundException;
import com.example.reidopitaco.exception.PhaseHasMatchesException;
import com.example.reidopitaco.mapper.PhaseGroupMapper;
import com.example.reidopitaco.repository.MatchRepository;
import com.example.reidopitaco.repository.PhaseGroupRepository;
import com.example.reidopitaco.repository.PhaseTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PhaseGroupService {

    private final PhaseService phaseService;
    private final PhaseGroupRepository groupRepository;
    private final PhaseTeamRepository phaseTeamRepository;
    private final MatchRepository matchRepository;
    private final PhaseGroupMapper mapper;

    public PhaseGroupService(
            PhaseService phaseService,
            PhaseGroupRepository groupRepository,
            PhaseTeamRepository phaseTeamRepository,
            MatchRepository matchRepository,
            PhaseGroupMapper mapper
    ) {
        this.phaseService = phaseService;
        this.groupRepository = groupRepository;
        this.phaseTeamRepository = phaseTeamRepository;
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PhaseGroupResponse create(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            CreatePhaseGroupRequest request
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);

        if (phase.getPhaseType() != TournamentPhaseType.GROUPS) {
            throw new GroupOnlyAllowedInGroupsPhaseException();
        }

        String name = request.name().trim();
        if (groupRepository.existsByPhaseIdAndNameIgnoreCase(phase.getId(), name)) {
            throw new GroupNameAlreadyInUseException();
        }

        int position = (int) groupRepository.countByPhaseId(phase.getId());
        PhaseGroup group = PhaseGroup.builder()
                .phase(phase)
                .name(name)
                .position(position)
                .build();
        return toResponse(groupRepository.save(group));
    }

    @Transactional(readOnly = true)
    public List<PhaseGroupResponse> list(UUID tournamentPublicId, UUID phasePublicId) {
        return groupRepository.findAllByPhasePublicIdOrderByPositionAsc(phasePublicId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PhaseGroupResponse update(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID groupPublicId,
            UpdatePhaseGroupRequest request
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);
        PhaseGroup group = loadGroup(phase.getPublicId(), groupPublicId);

        String newName = request.name().trim();
        if (!group.getName().equalsIgnoreCase(newName)
                && groupRepository.existsByPhaseIdAndNameIgnoreCase(phase.getId(), newName)) {
            throw new GroupNameAlreadyInUseException();
        }
        group.setName(newName);
        return toResponse(groupRepository.saveAndFlush(group));
    }

    @Transactional
    public void delete(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID groupPublicId
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);
        PhaseGroup group = loadGroup(phase.getPublicId(), groupPublicId);
        if (matchRepository.countByGroupId(group.getId()) > 0) {
            throw new PhaseHasMatchesException("group");
        }
        groupRepository.delete(group);
    }

    PhaseGroup loadGroup(UUID phasePublicId, UUID groupPublicId) {
        return groupRepository.findByPublicIdAndPhasePublicId(groupPublicId, phasePublicId)
                .orElseThrow(PhaseGroupNotFoundException::new);
    }

    private PhaseGroupResponse toResponse(PhaseGroup group) {
        long teamCount = phaseTeamRepository.findAllByPhasePublicId(group.getPhase().getPublicId())
                .stream()
                .filter(pt -> pt.getGroup() != null && pt.getGroup().getId().equals(group.getId()))
                .count();
        return mapper.toResponse(group, teamCount);
    }
}
