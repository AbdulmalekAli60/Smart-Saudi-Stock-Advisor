package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.dto.CreateCompanyDTO;
import com.SmartSaudiStockAdvisor.dto.SearchResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    List<CompanyInformationDTO> getAllCompanies();

    CompanyInformationDTO createCompany(CreateCompanyDTO createCompanyDTO);

    String deleteCompany(Long companyId);

    List<SearchResponseDTO> searchCompany(String keyword);

    CompanyInformationDTO getCompanyById(Long companyId);
}
