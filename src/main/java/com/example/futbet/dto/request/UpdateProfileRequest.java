package com.example.futbet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateProfileRequest(
        @NotBlank
        @Size(min = 2, max = 120)
        String name,

        @URL
        @Size(max = 500)
        String avatarUrl
) {
}
