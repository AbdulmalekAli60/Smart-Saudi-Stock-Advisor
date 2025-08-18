package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.HistoricalDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ETagService {

    String generateETag(Object data);

    @Service
    interface HistoricalDataService {
        List<HistoricalDataDTO> getHistoricalData();
    }
}
