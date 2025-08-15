package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {

    List<CompanyInformationDTO> getAllCompanies();
}
