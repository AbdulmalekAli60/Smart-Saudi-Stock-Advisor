package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.CompanyInformationDTO;
import com.SmartSaudiStockAdvisor.dto.CreateCompanyDTO;
import com.SmartSaudiStockAdvisor.dto.SearchResponseDTO;
import com.SmartSaudiStockAdvisor.entity.Company;
import com.SmartSaudiStockAdvisor.entity.Sector;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.exception.OperationFailedException;
import com.SmartSaudiStockAdvisor.repo.CompanyRepo;
import com.SmartSaudiStockAdvisor.repo.SectorRepo;
import com.SmartSaudiStockAdvisor.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;
    private final SectorRepo sectorRepo;
    private final MessageSource messageSource;

    @Autowired
    public CompanyServiceImpl(CompanyRepo companyRepo, SectorRepo sectorRepo, MessageSource source) {
        this.companyRepo = companyRepo;
        this.sectorRepo = sectorRepo;
        this.messageSource = source;
    }

    @Override
    public List<CompanyInformationDTO> getAllCompanies() {
        return companyRepo
                .findAll()
                .stream()
                .map(CompanyInformationDTO::new)
                .toList();
    }

    @Override
    public CompanyInformationDTO createCompany(CreateCompanyDTO createCompanyDTO) { // for admin

        if (companyRepo.existsByTickerName(createCompanyDTO.tickerName())) {
            String[] params = {createCompanyDTO.tickerName()};
            throw new AlreadyExistsException(getMessage("company-service.ticker-already-exist", params));
        }

        Long[] params = {createCompanyDTO.sectorId()};
        Sector sector = sectorRepo.findById(createCompanyDTO.sectorId())
                .orElseThrow(() -> new EntityNotFoundException(getMessage("company-service.sector.not-found", params)));

            Company newCompany = new Company();
            newCompany.setCompanyLogo(createCompanyDTO.companyLogo());
            newCompany.setCompanyArabicName(createCompanyDTO.companyArabicName());
            newCompany.setCompanyEnglishName(createCompanyDTO.companyEnglishName());
            newCompany.setTickerName(createCompanyDTO.tickerName());
            newCompany.setSector(sector);

            Company savedNewCompany = companyRepo.save(newCompany);
            log.info("New Company has been Added with Id: {}", savedNewCompany.getCompanyId());
            return new CompanyInformationDTO(savedNewCompany);
        }

    @Override
    @Transactional
    public String deleteCompany(Long companyId) { // for admin
        Long[] param = {companyId};
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(getMessage("company-service.delete.company.not-found", param)));

        try {
            companyRepo.delete(company);
            Object[] params = {company.getCompanyArabicName(), company.getCompanyId()};
            log.info("Company {} with ID: {} has been deleted", company.getCompanyEnglishName(), companyId);
            return getMessage("company-service.delete.success-message", params);
        } catch (DataIntegrityViolationException e) {
            Object[] errorParams = {company.getCompanyEnglishName(), e};
            throw new OperationFailedException(getMessage("company-service.delete.failed-message", errorParams));
        }
    }

    @Override
    public List<SearchResponseDTO> searchCompany(String keyword) {
        return companyRepo.searchCompanies(keyword)
                .stream()
                .map(SearchResponseDTO::new)
                .toList();
    }

    @Override
    public CompanyInformationDTO getCompanyById(Long companyId) {
        Long[] param = {companyId};
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntryNotFoundException(getMessage("company-service.get-all-companies.not-found-message", param)));
        return new CompanyInformationDTO(company);
    }

    private String getMessage(String key, Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}

