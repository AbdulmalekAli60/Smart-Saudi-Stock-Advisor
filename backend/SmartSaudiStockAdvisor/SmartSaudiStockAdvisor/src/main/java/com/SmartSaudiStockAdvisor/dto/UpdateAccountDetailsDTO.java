package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateAccountDetailsDTO(

        @Size(min = 3, max = 24, message = "{validation.update-account.name.size}")
        @Pattern(
                regexp = "^[a-zA-Z\\s'\\u0600-\\u06FF-]+$",
                message = "{validation.update-account.name.pattern}"
        )
        String name,

        @Size(min = 3, max = 24, message = "{validation.update-account.username.size}")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "{validation.update-account.username.pattern}"
        )
        String username,

        @Size(min = 8, max = 16, message = "{validation.update-account.password.size}")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
                message = "{validation.update-account.password.pattern}"
        )
        String password,

        @Email(message = "{validation.update-account.email.email}")
        String email,

        @DecimalMax(value = "99999999.99", message = "{validation.max-decimal.invest-amount}")
        @DecimalMin(value = "0", message = "{validation.min-decimal.invest-amount}")
        BigDecimal investAmount
) {
}