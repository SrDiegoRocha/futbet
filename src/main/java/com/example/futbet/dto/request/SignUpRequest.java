package com.example.futbet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record SignUpRequest(
        @NotBlank
        @Size(min = 2, max = 120)
        String name,

        @NotBlank
        @Email
        @Size(max = 180)
        String email,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @URL
        @Size(max = 500)
        String avatarUrl
) {
}
