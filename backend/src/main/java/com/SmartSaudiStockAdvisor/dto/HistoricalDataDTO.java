package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.HistoricalData;

import java.time.LocalDateTime;

public record HistoricalDataDTO(
        Long dataId,
        Float open,
        Float close,
        Float high,
        Float low,
        Long volume,
        LocalDateTime dataDate,
        Long companyId
) {

    public HistoricalDataDTO(HistoricalData historicalData){
        this(
            historicalData.getDataId(),
            historicalData.getOpen(),
            historicalData.getClose(),
            historicalData.getHigh(),
            historicalData.getLow(),
            historicalData.getVolume(),
            historicalData.getDataDate(),
            historicalData.getCompany().getCompanyId()
        );
    }
}
