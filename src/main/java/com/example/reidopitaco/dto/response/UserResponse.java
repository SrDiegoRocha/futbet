package com.example.reidopitaco.dto.response;

import com.example.reidopitaco.enums.Role;

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
