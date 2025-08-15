package com.SmartSaudiStockAdvisor.dto;

import java.util.List;

public class CompaniesResponseDTO {
    private List<CompanyInformationDTO> companyInformationDTOS;
    private int totalNumberOfCompanies;

    public CompaniesResponseDTO(int totalNumberOfCompanies, List<CompanyInformationDTO> companyInformationDTOS) {
        this.totalNumberOfCompanies = totalNumberOfCompanies;
        this.companyInformationDTOS = companyInformationDTOS;
    }

    public List<CompanyInformationDTO> getCompanyInformationDTOS() {
        return companyInformationDTOS;
    }

    public void setCompanyInformationDTOS(List<CompanyInformationDTO> companyInformationDTOS) {
        this.companyInformationDTOS = companyInformationDTOS;
    }

    public int getTotalNumberOfCompanies() {
        return totalNumberOfCompanies;
    }

    public void setTotalNumberOfCompanies(int totalNumberOfCompanies) {
        this.totalNumberOfCompanies = totalNumberOfCompanies;
    }
}
