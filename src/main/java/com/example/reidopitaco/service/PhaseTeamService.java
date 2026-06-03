package com.example.reidopitaco.service;

import com.example.reidopitaco.dto.request.MovePhaseTeamRequest;
import com.example.reidopitaco.dto.response.PhaseTeamResponse;
import com.example.reidopitaco.entity.PhaseGroup;
import com.example.reidopitaco.entity.PhaseTeam;
import com.example.reidopitaco.entity.Team;
import com.example.reidopitaco.entity.Tournament;
import com.example.reidopitaco.entity.TournamentPhase;
import com.example.reidopitaco.entity.TournamentTeam;
import com.example.reidopitaco.enums.TournamentPhaseType;
import com.example.reidopitaco.exception.NoGroupsToDrawException;
import com.example.reidopitaco.exception.PhaseGroupNotFoundException;
import com.example.reidopitaco.exception.PhaseHasMatchesException;
import com.example.reidopitaco.exception.PhaseTeamNotFoundException;
import com.example.reidopitaco.exception.TeamAlreadyInTournamentException;
import com.example.reidopitaco.exception.TeamNotInTournamentException;
import com.example.reidopitaco.mapper.PhaseTeamMapper;
import com.example.reidopitaco.repository.MatchRepository;
import com.example.reidopitaco.repository.PhaseGroupRepository;
import com.example.reidopitaco.repository.PhaseTeamRepository;
import com.example.reidopitaco.repository.TournamentTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PhaseTeamService {

    private final PhaseService phaseService;
    private final PhaseGroupRepository groupRepository;
    private final PhaseTeamRepository phaseTeamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;
    private final PhaseTeamMapper mapper;
    private final SecureRandom random = new SecureRandom();

    public PhaseTeamService(
            PhaseService phaseService,
            PhaseGroupRepository groupRepository,
            PhaseTeamRepository phaseTeamRepository,
            TournamentTeamRepository tournamentTeamRepository,
            MatchRepository matchRepository,
            PhaseTeamMapper mapper
    ) {
        this.phaseService = phaseService;
        this.groupRepository = groupRepository;
        this.phaseTeamRepository = phaseTeamRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.matchRepository = matchRepository;
        this.mapper = mapper;
    }

    @Transactional
    public PhaseTeamResponse add(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID teamPublicId
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);

        TournamentTeam tournamentTeam = tournamentTeamRepository
                .findByTournamentPublicIdAndTeamPublicId(tournamentPublicId, teamPublicId)
                .orElseThrow(TeamNotInTournamentException::new);

        if (phaseTeamRepository.existsByPhasePublicIdAndTeamPublicId(phasePublicId, teamPublicId)) {
            throw new TeamAlreadyInTournamentException();
        }

        PhaseTeam phaseTeam = PhaseTeam.builder()
                .phase(phase)
                .team(tournamentTeam.getTeam())
                .build();
        return mapper.toResponse(phaseTeamRepository.save(phaseTeam));
    }

    @Transactional(readOnly = true)
    public List<PhaseTeamResponse> list(UUID tournamentPublicId, UUID phasePublicId) {
        return phaseTeamRepository.findAllByPhasePublicId(phasePublicId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public PhaseTeamResponse move(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID teamPublicId,
            MovePhaseTeamRequest request
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);

        PhaseTeam phaseTeam = phaseTeamRepository
                .findByPhasePublicIdAndTeamPublicId(phasePublicId, teamPublicId)
                .orElseThrow(PhaseTeamNotFoundException::new);

        PhaseGroup group = null;
        if (request.groupId() != null) {
            group = groupRepository.findByPublicIdAndPhasePublicId(request.groupId(), phasePublicId)
                    .orElseThrow(PhaseGroupNotFoundException::new);
        }
        phaseTeam.setGroup(group);
        return mapper.toResponse(phaseTeamRepository.save(phaseTeam));
    }

    @Transactional
    public void remove(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId,
            UUID teamPublicId
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);
        PhaseTeam phaseTeam = phaseTeamRepository
                .findByPhasePublicIdAndTeamPublicId(phasePublicId, teamPublicId)
                .orElseThrow(PhaseTeamNotFoundException::new);
        if (matchRepository.countByPhaseAndTeam(phase.getId(), phaseTeam.getTeam().getId()) > 0) {
            throw new PhaseHasMatchesException("team from phase");
        }
        phaseTeamRepository.delete(phaseTeam);
    }

    @Transactional
    public List<PhaseTeamResponse> draw(
            UUID ownerPublicId,
            UUID tournamentPublicId,
            UUID phasePublicId
    ) {
        Tournament tournament = phaseService.loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentPhase phase = phaseService.loadPhase(tournament, phasePublicId);

        if (phase.getPhaseType() != TournamentPhaseType.GROUPS) {
            throw new com.example.reidopitaco.exception.GroupOnlyAllowedInGroupsPhaseException();
        }

        List<PhaseGroup> groups = groupRepository
                .findAllByPhasePublicIdOrderByPositionAsc(phasePublicId);
        if (groups.isEmpty()) {
            throw new NoGroupsToDrawException();
        }

        List<PhaseTeam> teamsToAssign = new ArrayList<>(
                phaseTeamRepository.findAllByPhaseIdAndGroupIsNull(phase.getId())
        );
        Collections.shuffle(teamsToAssign, random);

        for (int i = 0; i < teamsToAssign.size(); i++) {
            teamsToAssign.get(i).setGroup(groups.get(i % groups.size()));
        }
        phaseTeamRepository.saveAll(teamsToAssign);

        return phaseTeamRepository.findAllByPhasePublicId(phasePublicId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
