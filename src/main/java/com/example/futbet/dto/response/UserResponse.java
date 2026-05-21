package com.example.futbet.dto.response;

import com.example.futbet.enums.Role;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String avatarUrl,
        Role role,
        Instant createdAt
) {
}
