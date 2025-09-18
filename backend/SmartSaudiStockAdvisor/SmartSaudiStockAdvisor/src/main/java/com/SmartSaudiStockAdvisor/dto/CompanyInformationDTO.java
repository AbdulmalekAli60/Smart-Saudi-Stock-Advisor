package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Company;

public class CompanyInformationDTO {
    private Long companyId;
    private String companyLogo;
    private String tickerName;
    private Long sectorId;
    private String sectorArabicName;
    private String companyArabicName;
    private String companyEnglishName;
    private String sectorEnglishName;

    public CompanyInformationDTO() {
    }

    public CompanyInformationDTO (Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }

        this.companyId = company.getCompanyId();
        this.companyLogo = company.getCompanyLogo();
        this.tickerName = company.getTickerName();
        this.sectorId = company.getSector().getSectorId();
        this.sectorArabicName = company.getSector().getSectorArabicName();
        this.companyArabicName = company.getCompanyArabicName();
        this.companyEnglishName = company.getCompanyEnglishName();
        this.sectorEnglishName = company.getSector().getSectorEnglishName();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public String getSectorArabicName() {
        return sectorArabicName;
    }

    public void setSectorArabicName(String sectorArabicName) {
        this.sectorArabicName = sectorArabicName;
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

    public String getSectorEnglishName() {
        return sectorEnglishName;
    }

    public void setSectorEnglishName(String sectorEnglishName) {
        this.sectorEnglishName = sectorEnglishName;
    }
}
