package com.example.reidopitaco.dto.response;

import com.example.reidopitaco.enums.TournamentMemberRole;
import com.example.reidopitaco.enums.TournamentMemberStatus;

import java.time.Instant;
import java.util.UUID;

public record TournamentMemberResponse(
        UUID userId,
        String name,
        String avatarUrl,
        TournamentMemberRole role,
        TournamentMemberStatus status,
        Instant joinedAt,
        Instant leftAt,
        Instant bannedAt
) {
}
