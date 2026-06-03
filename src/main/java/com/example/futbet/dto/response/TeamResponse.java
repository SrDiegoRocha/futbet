package com.example.futbet.dto.response;

import com.example.futbet.enums.TeamType;

import java.time.Instant;
import java.util.UUID;

public record TeamResponse(
        UUID id,
        String name,
        String shortName,
        String badgeUrl,
        String primaryColor,
        String secondaryColor,
        boolean system,          // true = time padrão do sistema (não editável/deletável)
        TeamType teamType,       // CLUB ou NATIONAL_TEAM
        String countryCode,      // código flagicons (ex.: "br", "gb-eng"); só em seleções
        Instant createdAt,
        Instant updatedAt
) {
}
