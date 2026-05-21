package com.example.futbet.entity;

import com.example.futbet.enums.MatchGenerationMode;
import com.example.futbet.enums.MatchLegMode;
import com.example.futbet.enums.TournamentPhaseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "tournament_phases",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_id", "position"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false, updatable = false)
    private Tournament tournament;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false)
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase_type", nullable = false, length = 15)
    private TournamentPhaseType phaseType;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_leg_mode", nullable = false, length = 15)
    private MatchLegMode matchLegMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_generation_mode", nullable = false, length = 15)
    private MatchGenerationMode matchGenerationMode;

    @Column(name = "qualifiers_per_group")
    private Integer qualifiersPerGroup;

    @Column(name = "plays_inside_group_only")
    private Boolean playsInsideGroupOnly;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
