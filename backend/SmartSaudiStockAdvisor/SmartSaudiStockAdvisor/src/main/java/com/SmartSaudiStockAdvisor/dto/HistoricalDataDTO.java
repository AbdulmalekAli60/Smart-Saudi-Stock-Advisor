package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.HistoricalData;

import java.sql.Timestamp;

public class HistoricalDataDTO {

    private Long dataId;
    private Float open;
    private Float close;
    private Float high;
    private Float low;
    private Long volume;
    private Timestamp dataDate;
    private Long companyId;

    public HistoricalDataDTO() {
    }

    public HistoricalDataDTO(HistoricalData historicalData) {
        this.dataId = historicalData.getDataId();
        this.open = historicalData.getOpen();
        this.close = historicalData.getClose();
        this.high = historicalData.getHigh();
        this.low = historicalData.getLow();
        this.volume = historicalData.getVolume();
        this.dataDate = historicalData.getDataDate();
        this.companyId = historicalData.getCompany().getCompanyId();
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    public Float getHigh() {
        return high;
    }

    public void setHigh(Float high) {
        this.high = high;
    }

    public Float getLow() {
        return low;
    }

    public void setLow(Float low) {
        this.low = low;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Timestamp getDataDate() {
        return dataDate;
    }

    public void setDataDate(Timestamp dataDate) {
        this.dataDate = dataDate;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
