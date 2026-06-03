package com.example.reidopitaco.repository;

import com.example.reidopitaco.entity.TournamentTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TournamentTeamRepository extends JpaRepository<TournamentTeam, Long> {

    Optional<TournamentTeam> findByTournamentPublicIdAndTeamPublicId(
            UUID tournamentPublicId,
            UUID teamPublicId
    );

    Page<TournamentTeam> findAllByTournamentPublicId(UUID tournamentPublicId, Pageable pageable);

    List<TournamentTeam> findAllByTournamentPublicId(UUID tournamentPublicId);

    long countByTournamentId(Long tournamentId);

    boolean existsByTournamentPublicIdAndTeamPublicId(UUID tournamentPublicId, UUID teamPublicId);
}
