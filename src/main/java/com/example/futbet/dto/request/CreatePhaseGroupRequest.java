package com.example.futbet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePhaseGroupRequest(
        @NotBlank @Size(min = 1, max = 40) String name
) {
}
