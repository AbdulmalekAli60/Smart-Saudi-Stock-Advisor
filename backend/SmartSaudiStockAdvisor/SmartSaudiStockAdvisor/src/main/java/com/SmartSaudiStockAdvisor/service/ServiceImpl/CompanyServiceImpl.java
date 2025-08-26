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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;
    private final SectorRepo sectorRepo;

    @Autowired
    public CompanyServiceImpl(CompanyRepo companyRepo, SectorRepo sectorRepo) {
        this.companyRepo = companyRepo;
        this.sectorRepo = sectorRepo;
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

        if (companyRepo.existsByTickerName(createCompanyDTO.getTickerName())) {
            throw new AlreadyExistsException("Company with ticker '" +
                    createCompanyDTO.getTickerName() + "' already exists");
        }

        Sector sector = sectorRepo.findById(createCompanyDTO.getSectorId())
                .orElseThrow(() -> new EntityNotFoundException("Sector with ID " +
                        createCompanyDTO.getSectorId() + " not found"));

            Company newCompany = new Company();
            newCompany.setCompanyLogo(createCompanyDTO.getCompanyLogo());
            newCompany.setCompanyArabicName(createCompanyDTO.getCompanyArabicName());
            newCompany.setCompanyEnglishName(createCompanyDTO.getCompanyEnglishName());
            newCompany.setTickerName(createCompanyDTO.getTickerName());
            newCompany.setSector(sector);

            Company savedNewCompany = companyRepo.save(newCompany);
            log.info("New Company has been Added with Id: {}", savedNewCompany.getCompanyId());
            return new CompanyInformationDTO(savedNewCompany);
        }

    @Override
    @Transactional
    public String deleteCompany(Long companyId) { // for admin
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company with ID " + companyId + " does not exist"));

        try {
            companyRepo.delete(company);
            log.info("Company {} with ID: {} has been deleted", company.getCompanyEnglishName(), companyId);
            return "Company: " + company.getCompanyEnglishName() + " with ID: " + companyId + " has been deleted";
        } catch (DataIntegrityViolationException e) {
            throw new OperationFailedException("Cannot delete company '" + company.getCompanyEnglishName() +
                    "' because it has associated records" + e);
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
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new EntryNotFoundException("Company Dose not exists. Id: " + companyId));
        return new CompanyInformationDTO(company);
    }
}

