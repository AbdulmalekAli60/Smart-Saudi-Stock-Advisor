package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateCompanyDTO(
        @NotBlank(message = "{validation.not-blank.company.logo}")
        String companyLogo,

        @NotBlank(message = "{validation.not-blank.company.ticker}")
        String tickerName,

        @NotNull(message = "{validation.not-null.company.sector}")
        @Positive(message = "{validation.positive.company.sector}")
        Long sectorId,

        @NotBlank(message = "{validation.not-blank.company.arabic-name}")
        @Pattern(regexp = "^[\\u0600-\\u06FF\\s]+$", message = "{validation.pattern.company.arabic-name}")
        String companyArabicName,

        @NotBlank(message = "{validation.not-blank.company.english-name}")
        @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{validation.pattern.company.english-name}")
        String companyEnglishName
) {
}
