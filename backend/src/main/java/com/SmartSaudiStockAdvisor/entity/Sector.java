package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sector_id")
    private Long sectorId;

    @Column(name = "sector_Arabic_name", nullable = false, length = 255)
    private String sectorArabicName;

    @Column(name = "sector_english_name", nullable = false, length = 255)
    private String sectorEnglishName;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Company> companies;

    public Sector() {
    }

    public Sector(Long sectorId, String sectorArabicName, String sectorEnglishName, List<Company> companies) {
        this.sectorId = sectorId;
        this.sectorArabicName = sectorArabicName;
        this.sectorEnglishName = sectorEnglishName;
        this.companies = companies;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorArabicName() {
        return sectorArabicName;
    }

    public void setSectorArabicName(String sectorArabicName) {
        this.sectorArabicName = sectorArabicName;
    }

    public String getSectorEnglishName() {
        return sectorEnglishName;
    }

    public void setSectorEnglishName(String sectorEnglishName) {
        this.sectorEnglishName = sectorEnglishName;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "Sector{" +
                "sectorId=" + sectorId +
                ", sectorArabicName='" + sectorArabicName + '\'' +
                ", sectorEnglishName='" + sectorEnglishName + '\'' +
                ", companies=" + companies +
                '}';
    }
}
