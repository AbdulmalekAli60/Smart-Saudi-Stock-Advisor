package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LogInDTO(

        @NotBlank(message = "{validation.log-in.email.not-blank}")
        @Email(message = "{validation.log-in.email.email}")
        String email,

        @NotBlank(message = "{validation.log-in.password.not-blank}")
        @Size(min = 8, max = 16, message = "{validation.log-in.password.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "{validation.log-in.password.pattern}"
        )
        String password
) {
}
