package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.PredictionResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PredictionService {
    List<PredictionResponseDTO> getPredictionsByCompanyId(Long companyId);

    PredictionResponseDTO getLatestPredictionByCompanyId(Long companyId);
}
