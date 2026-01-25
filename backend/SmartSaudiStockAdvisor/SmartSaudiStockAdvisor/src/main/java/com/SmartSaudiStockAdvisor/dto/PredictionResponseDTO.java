package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Prediction;

import java.time.LocalDateTime;

public record PredictionResponseDTO(
                                   Long predictionId,
                                   LocalDateTime predictionDate,
                                   float prediction,
                                   boolean direction,
                                   LocalDateTime expirationDate,
                                   Float actualResult,
                                   Long CompanyId
                                   ) {
    public PredictionResponseDTO(Prediction prediction){
        this(
                prediction.getPredictionId(),
                prediction.getPredictionDate(),
                prediction.getPrediction(),
                prediction.getDirection(),
                prediction.getExpirationDate(),
                prediction.getActualResult(),
                prediction.getCompany().getCompanyId()
        );
    }
}
