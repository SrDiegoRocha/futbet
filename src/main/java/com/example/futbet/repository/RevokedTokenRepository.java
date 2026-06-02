package com.example.futbet.repository;

import com.example.futbet.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

    boolean existsByJti(UUID jti);
}
