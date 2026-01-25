package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Company;
import java.util.Objects;

public record CompanyInformationDTO(
        Long companyId,
        String companyLogo,
        String tickerName,
        Long sectorId,
        String sectorArabicName,
        String companyArabicName,
        String companyEnglishName,
        String sectorEnglishName
) {

    public CompanyInformationDTO(Company company) {
        this(
                Objects.requireNonNull(company, "Company cannot be null").getCompanyId(),
                company.getCompanyLogo(),
                company.getTickerName(),
                company.getSector().getSectorId(),
                company.getSector().getSectorArabicName(),
                company.getCompanyArabicName(),
                company.getCompanyEnglishName(),
                company.getSector().getSectorEnglishName()
        );
    }
}