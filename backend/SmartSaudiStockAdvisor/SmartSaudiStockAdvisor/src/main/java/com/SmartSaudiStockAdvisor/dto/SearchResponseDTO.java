package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Company;

public class SearchResponseDTO {

    private Long companyId;
    private String companyArabicName;

    public SearchResponseDTO() {
    }

    public SearchResponseDTO(Company company) {
        this.companyId = company.getCompanyId();
        this.companyArabicName = company.getCompanyArabicName();
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyArabicName() {
        return companyArabicName;
    }

    public void setCompanyArabicName(String companyArabicName) {
        this.companyArabicName = companyArabicName;
    }
}



