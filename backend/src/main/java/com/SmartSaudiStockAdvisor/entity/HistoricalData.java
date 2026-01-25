package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historical_data")
public class HistoricalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_id")
    private Long dataId;

    @Column(name = "open")
    private Float open;

    @Column(name = "close")
    private Float close;

    @Column(name = "high")
    private Float high;

    @Column(name = "low")
    private Float low;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "data_date") // initialize in def constructor
    private LocalDateTime dataDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public HistoricalData() {
        this.dataDate = LocalDateTime.now();
    }

    public HistoricalData(Long dataId, Company company, Long volume, Float low, Float high, Float close, Float open) {
        this();
        this.dataId = dataId;
        this.company = company;
        this.volume = volume;
        this.low = low;
        this.high = high;
        this.close = close;
        this.open = open;
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

    public LocalDateTime getDataDate() {
        return dataDate;
    }

    public void setDataDate(LocalDateTime dataDate) {
        this.dataDate = dataDate;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "HistoricalData{" +
                "dataId=" + dataId +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", dataDate=" + dataDate +
                ", company=" + company +
                '}';
    }
}
