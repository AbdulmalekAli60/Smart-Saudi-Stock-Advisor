package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.repo.CompanyRepo;
import com.SmartSaudiStockAdvisor.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    @Autowired
    public CompanyServiceImpl(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    @Override
    public List<CompanyInformationDTO> getAllCompanies() {
        return companyRepo
                .findAll()
                .stream()
                .map(CompanyInformationDTO::new)
                .toList();
    }
}
