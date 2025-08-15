package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Company;

public class CompanyInformationDTO {
    private Long companyId;
    private String companyLogo;
    private String tickerName;
    private Long sectorId;
    private String sectorName;
    private String companyArabicName;
    private String getCompanyEnglishName;
    private int numberOfCompanies;

    public CompanyInformationDTO (Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }

        this.companyId = company.getCompanyId();
        this.companyLogo = company.getCompanyLogo();
        this.tickerName = company.getTickerName();
        this.sectorId = company.getSector().getSectorId();
        this.sectorName = company.getSector().getSectorName();
        this.companyArabicName = company.getCompanyArabicName();
        this.getCompanyEnglishName = company.getCompanyEnglishName();
//        this.numberOfCompanies = co;
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

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getCompanyArabicName() {
        return companyArabicName;
    }

    public void setCompanyArabicName(String companyArabicName) {
        this.companyArabicName = companyArabicName;
    }

    public String getGetCompanyEnglishName() {
        return getCompanyEnglishName;
    }

    public void setGetCompanyEnglishName(String getCompanyEnglishName) {
        this.getCompanyEnglishName = getCompanyEnglishName;
    }

    public int getNumberOfCompanies() {
        return numberOfCompanies;
    }

    public void setNumberOfCompanies(int numberOfCompanies) {
        this.numberOfCompanies = numberOfCompanies;
    }
}
