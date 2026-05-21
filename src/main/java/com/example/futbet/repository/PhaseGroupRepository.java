package com.example.futbet.repository;

import com.example.futbet.entity.PhaseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhaseGroupRepository extends JpaRepository<PhaseGroup, Long> {

    Optional<PhaseGroup> findByPublicIdAndPhasePublicId(UUID groupPublicId, UUID phasePublicId);

    List<PhaseGroup> findAllByPhasePublicIdOrderByPositionAsc(UUID phasePublicId);

    long countByPhaseId(Long phaseId);

    boolean existsByPhaseIdAndNameIgnoreCase(Long phaseId, String name);
}
