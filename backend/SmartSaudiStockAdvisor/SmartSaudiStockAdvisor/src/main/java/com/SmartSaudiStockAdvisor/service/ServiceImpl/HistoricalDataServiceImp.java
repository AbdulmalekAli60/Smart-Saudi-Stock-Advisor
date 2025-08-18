package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.HistoricalDataDTO;
import com.SmartSaudiStockAdvisor.repo.HistoricalDataRepo;
import com.SmartSaudiStockAdvisor.service.HistoricalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalDataServiceImp implements HistoricalDataService {

    private final HistoricalDataRepo historicalDataRepo;

    @Autowired
    public HistoricalDataServiceImp(HistoricalDataRepo historicalDataRepo) {
        this.historicalDataRepo = historicalDataRepo;
    }

    @Override
    public List<HistoricalDataDTO> getHistoricalDataByCompanyId(Long companyId) {
        return historicalDataRepo.findHistoricalDataByCompanyId(companyId)
                .stream()
                .map(HistoricalDataDTO::new)
                .toList();
    }
}
