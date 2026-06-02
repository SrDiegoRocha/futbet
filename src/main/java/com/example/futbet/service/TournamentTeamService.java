package com.example.futbet.service;

import com.example.futbet.dto.response.TournamentTeamResponse;
import com.example.futbet.entity.Team;
import com.example.futbet.entity.Tournament;
import com.example.futbet.entity.TournamentTeam;
import com.example.futbet.enums.TournamentStatus;
import com.example.futbet.exception.NotTournamentOwnerException;
import com.example.futbet.exception.TeamAlreadyInTournamentException;
import com.example.futbet.exception.TeamNotFoundException;
import com.example.futbet.exception.TeamNotOwnedException;
import com.example.futbet.exception.TournamentFullException;
import com.example.futbet.exception.TournamentNotEditableException;
import com.example.futbet.exception.TournamentNotFoundException;
import com.example.futbet.mapper.TournamentTeamMapper;
import com.example.futbet.repository.TeamRepository;
import com.example.futbet.repository.TournamentRepository;
import com.example.futbet.repository.TournamentTeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class TournamentTeamService {

    /**
     * Os campos expostos no {@code TournamentTeamResponse} pertencem ao {@code Team} aninhado,
     * não ao {@code TournamentTeam} que é a entidade paginada. Este mapa traduz o nome que o
     * cliente envia (ex.: {@code name}) para o caminho real na entidade (ex.: {@code team.name}),
     * evitando {@code PropertyReferenceException} ao montar a ordenação.
     */
    private static final Map<String, String> SORT_PROPERTY_MAPPING = Map.of(
            "teamId", "team.publicId",
            "name", "team.name",
            "shortName", "team.shortName",
            "badgeUrl", "team.badgeUrl",
            "primaryColor", "team.primaryColor",
            "secondaryColor", "team.secondaryColor",
            "addedAt", "addedAt"
    );

    private final TournamentRepository tournamentRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TeamRepository teamRepository;
    private final TournamentTeamMapper mapper;

    public TournamentTeamService(
            TournamentRepository tournamentRepository,
            TournamentTeamRepository tournamentTeamRepository,
            TeamRepository teamRepository,
            TournamentTeamMapper mapper
    ) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentTeamRepository = tournamentTeamRepository;
        this.teamRepository = teamRepository;
        this.mapper = mapper;
    }

    @Transactional
    public TournamentTeamResponse link(UUID ownerPublicId, UUID tournamentPublicId, UUID teamPublicId) {
        Tournament tournament = loadOwnedEditable(ownerPublicId, tournamentPublicId);

        Team team = teamRepository
                .findByPublicIdAndOwnerPublicIdAndActiveTrue(teamPublicId, ownerPublicId)
                .orElseThrow(TeamNotOwnedException::new);

        if (tournamentTeamRepository.existsByTournamentPublicIdAndTeamPublicId(tournamentPublicId, teamPublicId)) {
            throw new TeamAlreadyInTournamentException();
        }

        if (tournament.getMaxTeams() != null) {
            long current = tournamentTeamRepository.countByTournamentId(tournament.getId());
            if (current >= tournament.getMaxTeams()) {
                throw new TournamentFullException("teams");
            }
        }

        TournamentTeam link = TournamentTeam.builder()
                .tournament(tournament)
                .team(team)
                .build();
        return mapper.toResponse(tournamentTeamRepository.save(link));
    }

    @Transactional
    public void unlink(UUID ownerPublicId, UUID tournamentPublicId, UUID teamPublicId) {
        loadOwnedEditable(ownerPublicId, tournamentPublicId);
        TournamentTeam link = tournamentTeamRepository
                .findByTournamentPublicIdAndTeamPublicId(tournamentPublicId, teamPublicId)
                .orElseThrow(TeamNotFoundException::new);
        tournamentTeamRepository.delete(link);
    }

    @Transactional(readOnly = true)
    public Page<TournamentTeamResponse> list(UUID tournamentPublicId, Pageable pageable) {
        if (!tournamentRepository.findByPublicIdAndActiveTrue(tournamentPublicId).isPresent()) {
            throw new TournamentNotFoundException();
        }
        return tournamentTeamRepository.findAllByTournamentPublicId(tournamentPublicId, remapSort(pageable))
                .map(mapper::toResponse);
    }

    private Pageable remapSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return pageable;
        }
        Sort mappedSort = Sort.by(
                pageable.getSort().stream()
                        .map(order -> order.withProperty(
                                SORT_PROPERTY_MAPPING.getOrDefault(order.getProperty(), order.getProperty())
                        ))
                        .toList()
        );
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), mappedSort);
    }

    private Tournament loadOwnedEditable(UUID ownerPublicId, UUID tournamentPublicId) {
        Tournament tournament = tournamentRepository.findByPublicIdAndActiveTrue(tournamentPublicId)
                .orElseThrow(TournamentNotFoundException::new);
        if (!tournament.getOwner().getPublicId().equals(ownerPublicId)) {
            throw new NotTournamentOwnerException();
        }
        if (tournament.getStatus() == TournamentStatus.IN_PROGRESS
                || tournament.getStatus() == TournamentStatus.FINISHED) {
            throw new TournamentNotEditableException(
                    tournament.getStatus(),
                    "teams can only be changed in DRAFT or OPEN"
            );
        }
        return tournament;
    }
}
