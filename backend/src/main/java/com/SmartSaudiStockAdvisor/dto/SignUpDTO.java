package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
        @NotBlank(message = "{validation.sign-up.email.not-blank}")
        @Email(message = "{validation.sign-up.email.email}")
        String email,

        @NotBlank(message = "{validation.sign-up.name.not-blank}")
        @Size(min = 3, max = 24, message = "{validation.sign-up.name.size}")
        @Pattern(
                regexp = "^[a-zA-Z\\s'\\u0600-\\u06FF-]+$",
                message = "{validation.sign-up.name.pattern}"
        )
        String name,

        @NotBlank(message = "{validation.sign-up.password.not-blank}")
        @Size(min = 8, max = 16, message = "{validation.sign-up.password.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "{validation.sign-up.password.pattern}"
        )
        String password,

        @NotBlank(message = "{validation.sign-up.username.not-blank}")
        @Size(min = 3, max = 24, message = "{validation.sign-up.username.size}")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "{validation.sign-up.username.pattern}"
        )
        String username
) {}
