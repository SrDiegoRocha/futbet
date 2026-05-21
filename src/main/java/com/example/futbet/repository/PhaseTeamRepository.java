package com.example.futbet.repository;

import com.example.futbet.entity.PhaseTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhaseTeamRepository extends JpaRepository<PhaseTeam, Long> {

    Optional<PhaseTeam> findByPhasePublicIdAndTeamPublicId(UUID phasePublicId, UUID teamPublicId);

    List<PhaseTeam> findAllByPhasePublicId(UUID phasePublicId);

    List<PhaseTeam> findAllByPhaseIdAndGroupIsNull(Long phaseId);

    long countByPhaseId(Long phaseId);

    boolean existsByPhasePublicIdAndTeamPublicId(UUID phasePublicId, UUID teamPublicId);
}
