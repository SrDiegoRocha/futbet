package com.example.reidopitaco.dto.request;

import com.example.reidopitaco.enums.TournamentPrivacy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTournamentRequest(
        @NotBlank @Size(min = 3, max = 80) String name,
        @Size(max = 500) String description,
        @NotNull TournamentPrivacy privacy,
        @Min(2) Integer maxParticipants,
        @Min(2) Integer maxTeams,
        @NotNull @Valid TournamentSettingsPayload settings
) {
}
