package com.example.futbet.repository;

import com.example.futbet.entity.TournamentPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TournamentPhaseRepository extends JpaRepository<TournamentPhase, Long> {

    Optional<TournamentPhase> findByPublicIdAndTournamentPublicId(UUID phasePublicId, UUID tournamentPublicId);

    List<TournamentPhase> findAllByTournamentPublicIdOrderByPositionAsc(UUID tournamentPublicId);

    long countByTournamentId(Long tournamentId);

    @Modifying
    @Query("""
            UPDATE TournamentPhase p
               SET p.position = p.position + :delta
             WHERE p.tournament.id = :tournamentId
               AND p.position >= :fromPosition
               AND p.position <= :toPosition
            """)
    void shiftPositions(
            @Param("tournamentId") Long tournamentId,
            @Param("fromPosition") int fromPosition,
            @Param("toPosition") int toPosition,
            @Param("delta") int delta
    );
}
