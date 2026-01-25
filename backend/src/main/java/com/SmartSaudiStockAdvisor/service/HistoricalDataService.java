package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.HistoricalDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoricalDataService {
    List<HistoricalDataDTO> getHistoricalDataByCompanyId(Long companyId);
}
