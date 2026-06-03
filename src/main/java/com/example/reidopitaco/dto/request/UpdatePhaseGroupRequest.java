package com.example.reidopitaco.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePhaseGroupRequest(
        @NotBlank @Size(min = 1, max = 40) String name
) {
}
