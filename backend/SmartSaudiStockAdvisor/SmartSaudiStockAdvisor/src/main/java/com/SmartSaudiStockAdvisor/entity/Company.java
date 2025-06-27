package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "company_logo", nullable = false, columnDefinition = "TEXT")
    private String companyLogo;

    @Column(name = "ticker_name", nullable = false, length = 255)
    private String tickerName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WatchList> watchLists;

    @OneToMany(mappedBy = "company" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricalData> historicalData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prediction> predictions;

    public Company() {
    }

    public Company(Long companyId, List<Prediction> predictions, Sector sector, List<HistoricalData> historicalData, List<WatchList> watchLists, String tickerName, String companyLogo, String companyName) {
        this.companyId = companyId;
        this.predictions = predictions;
        this.sector = sector;
        this.historicalData = historicalData;
        this.watchLists = watchLists;
        this.tickerName = tickerName;
        this.companyLogo = companyLogo;
        this.companyName = companyName;
    }

    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    public void setWatchLists(List<WatchList> watchLists) {
        this.watchLists = watchLists;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public List<HistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(List<HistoricalData> historicalData) {
        this.historicalData = historicalData;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", watchLists=" + watchLists +
                ", historicalData=" + historicalData +
                ", sector=" + sector +
                ", predictions=" + predictions +
                '}';
    }
}
