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

    @Column(name = "sector_name", nullable = false, length = 255)
    private String sectorName;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Company> companies;

    public Sector() {
    }

    public Sector(Long sectorId, List<Company> companies, String sectorName) {
        this.sectorId = sectorId;
        this.companies = companies;
        this.sectorName = sectorName;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
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
                ", sectorName='" + sectorName + '\'' +
                ", companies=" + companies +
                '}';
    }
}
