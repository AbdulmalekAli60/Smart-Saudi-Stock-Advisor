package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Sector;

public class SectorResponseDTO {
    private Long sectorId;
    private String sectorArabicName;
    private String sectorEnglishName;

    public SectorResponseDTO(Sector sector) {
        this.sectorId = sector.getSectorId();
        this.sectorArabicName = sector.getSectorArabicName();
        this.sectorEnglishName = sector.getSectorEnglishName();
    }

    public String getSectorEnglishName() {
        return sectorEnglishName;
    }

    public void setSectorEnglishName(String sectorEnglishName) {
        this.sectorEnglishName = sectorEnglishName;
    }

    public String getSectorArabicName() {
        return sectorArabicName;
    }

    public void setSectorArabicName(String sectorArabicName) {
        this.sectorArabicName = sectorArabicName;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }
}
