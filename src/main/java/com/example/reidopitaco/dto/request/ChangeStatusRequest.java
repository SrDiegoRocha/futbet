package com.example.reidopitaco.dto.request;

import com.example.reidopitaco.enums.TournamentStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(
        @NotNull TournamentStatus targetStatus
) {
}
