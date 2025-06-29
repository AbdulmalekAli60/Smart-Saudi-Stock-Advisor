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

    @Column(name = "company_arabic_name", nullable = false, length = 255)
    private String companyArabicName;

    @Column(name = "company_english_name", nullable = false, length = 255)
    private String companyEnglishName;

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

    public Company(Long companyId, String companyArabicName, String companyEnglishName, String companyLogo, String tickerName, List<WatchList> watchLists, List<HistoricalData> historicalData, Sector sector, List<Prediction> predictions) {
        this.companyId = companyId;
        this.companyArabicName = companyArabicName;
        this.companyEnglishName = companyEnglishName;
        this.companyLogo = companyLogo;
        this.tickerName = tickerName;
        this.watchLists = watchLists;
        this.historicalData = historicalData;
        this.sector = sector;
        this.predictions = predictions;
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

    public String getCompanyArabicName() {
        return companyArabicName;
    }

    public void setCompanyArabicName(String companyArabicName) {
        this.companyArabicName = companyArabicName;
    }

    public String getCompanyEnglishName() {
        return companyEnglishName;
    }

    public void setCompanyEnglishName(String companyEnglishName) {
        this.companyEnglishName = companyEnglishName;
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
                ", companyArabicName='" + companyArabicName + '\'' +
                ", companyEnglishName='" + companyEnglishName + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", watchLists=" + watchLists +
                ", historicalData=" + historicalData +
                ", sector=" + sector +
                ", predictions=" + predictions +
                '}';
    }
}
