package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.CompaniesResponseDTO;
import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping(value = "/companies")
    public ResponseEntity<CompaniesResponseDTO> getAllCompanies(){
        List<CompanyInformationDTO> allCompanies = companyService.getAllCompanies();
        CompaniesResponseDTO companiesResponseDTO = new CompaniesResponseDTO(allCompanies.size(), allCompanies);

        return ResponseEntity.status(HttpStatus.OK).body(companiesResponseDTO);
    }
}
