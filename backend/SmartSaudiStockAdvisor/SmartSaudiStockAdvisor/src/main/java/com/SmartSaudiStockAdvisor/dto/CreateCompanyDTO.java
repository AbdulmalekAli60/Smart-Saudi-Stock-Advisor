package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class CreateCompanyDTO {
    @NotBlank(message = "{validation.not-blank.company.logo}")
    private String companyLogo;

    @NotBlank(message = "{validation.not-blank.company.ticker}")
    private String tickerName;

    @NotNull(message = "{validation.not-null.company.sector}")
    @Positive(message = "{validation.positive.company.sector}")
    private Long sectorId;

    @NotBlank(message = "{validation.not-blank.company.arabic-name}")
    @Pattern(regexp = "^[\\u0600-\\u06FF\\s]+$", message = "{validation.pattern.company.arabic-name}")
    private String companyArabicName;

    @NotBlank(message = "{validation.not-blank.company.english-name}")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "{validation.pattern.company.english-name}")
    private String companyEnglishName;

    public CreateCompanyDTO() {
    }

    public CreateCompanyDTO(String companyLogo, String tickerName, Long sectorId, String companyArabicName, String companyEnglishName) {
        this.companyLogo = companyLogo;
        this.tickerName = tickerName;
        this.sectorId = sectorId;
        this.companyArabicName = companyArabicName;
        this.companyEnglishName = companyEnglishName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getCompanyArabicName() {
        return companyArabicName;
    }

    public void setCompanyArabicName(String companyArabicName) {
        this.companyArabicName = companyArabicName;
    }

    public String getCompanyEnglishName() {
        return companyEnglishName;
    }

    public void setCompanyEnglishName(String companyEnglishName) {
        this.companyEnglishName = companyEnglishName;
    }
}
