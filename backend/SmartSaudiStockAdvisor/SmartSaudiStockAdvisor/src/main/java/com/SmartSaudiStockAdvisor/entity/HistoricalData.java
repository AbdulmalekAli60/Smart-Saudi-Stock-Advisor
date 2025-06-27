package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;


import java.sql.Timestamp;

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
    private Timestamp dataDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
