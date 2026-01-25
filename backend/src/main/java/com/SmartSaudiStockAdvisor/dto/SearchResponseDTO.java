package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Company;

public record SearchResponseDTO(Long companyId, String companyArabicName) {

    public SearchResponseDTO(Company company) {
        this(company.getCompanyId(), company.getCompanyArabicName());
    }
}
