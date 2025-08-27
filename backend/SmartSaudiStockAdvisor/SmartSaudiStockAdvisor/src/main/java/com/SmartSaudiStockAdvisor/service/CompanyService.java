package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.dto.CreateCompanyDTO;
import com.SmartSaudiStockAdvisor.dto.SearchResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    List<CompanyInformationDTO> getAllCompanies();

    @PreAuthorize(value = "hasRole('ADMIN')")
    CompanyInformationDTO createCompany(CreateCompanyDTO createCompanyDTO);

    @PreAuthorize(value = "hasRole('ADMIN')")
    String deleteCompany(Long companyId);

    List<SearchResponseDTO> searchCompany(String keyword);

    CompanyInformationDTO getCompanyById(Long companyId);
}
