package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.dto.SearchResponseDTO;
import com.SmartSaudiStockAdvisor.service.CompanyService;
import com.SmartSaudiStockAdvisor.service.ETagService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@RestController
@RequestMapping(value = "/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final ETagService eTagService;

    @Autowired
    public CompanyController(CompanyService companyService, ETagService eTagService) {
        this.companyService = companyService;
        this.eTagService = eTagService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CompanyInformationDTO>> getAllCompanies(HttpServletRequest httpServletRequest){
        List<CompanyInformationDTO> companies = companyService.getAllCompanies();

        String currentETag = eTagService.generateETag(companies);
        String clientETag = httpServletRequest.getHeader("If-None-Match");
        if(currentETag.equals(clientETag)){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).mustRevalidate())
                .eTag(currentETag)
                .body(companies);
    }

    @GetMapping(value = "/search-company") // /companies/search-company?term=value
    public ResponseEntity<List<SearchResponseDTO>> searchCompany(@RequestParam(value = "term") String keyword){
        return  ResponseEntity.status(HttpStatus.OK).body(companyService.searchCompany(keyword));
    }

    @GetMapping(value = "/{company-id}")
    public ResponseEntity<CompanyInformationDTO> getCompanyById(@PathVariable(value = "company-id") Long companyId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.getCompanyById(companyId));
    }
}
