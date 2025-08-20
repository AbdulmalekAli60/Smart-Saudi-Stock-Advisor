package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class CreateCompanyDTO {
    @NotBlank(message = "Please Enter Company Logo")
    private String companyLogo;

    @NotBlank(message = "Please Enter Company Ticker")
    private String tickerName;

    @NotNull(message = "Please select a sector")
    @Positive(message = "Sector ID must be a positive number")
    private Long sectorId;

    @NotBlank(message = "Please Enter Company Arabic Name")
    @Pattern(regexp = "^[\\u0600-\\u06FF\\s]+$", message = "Please Enter Arabic Name with Arabic Letters")
    private String companyArabicName;

    @NotBlank(message = "Please Enter Company English Name")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Please Enter English Name with English Letters")
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
