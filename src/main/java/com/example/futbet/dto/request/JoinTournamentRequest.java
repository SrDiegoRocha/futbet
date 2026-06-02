package com.example.futbet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JoinTournamentRequest(
        @NotBlank @Size(min = 6, max = 6) String inviteCode
) {
}
