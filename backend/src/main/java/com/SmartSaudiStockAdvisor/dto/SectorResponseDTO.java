package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Sector;

public record SectorResponseDTO(Long sectorId,
                                String sectorArabicName,
                                String sectorEnglishName
                                ) {

    public SectorResponseDTO(Sector sector){
        this(
                sector.getSectorId(),
                sector.getSectorArabicName(),
                sector.getSectorEnglishName()
        );
    }
}
