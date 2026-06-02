package com.example.futbet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Refresh token revogado, identificado pelo seu {@code jti}. Presença na tabela = token inválido
 * para refresh, mesmo que ainda não tenha expirado. Alimentada pelo logout e pela rotação no refresh.
 */
@Entity
@Table(name = "revoked_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID jti;

    @Column(name = "user_public_id")
    private UUID userPublicId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt;

    @PrePersist
    void onCreate() {
        if (revokedAt == null) {
            revokedAt = Instant.now();
        }
    }
}
