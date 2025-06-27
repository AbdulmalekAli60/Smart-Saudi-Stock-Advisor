package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "prediction")
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Long predictionId;

    @Column(name = "prediction_date")
    private Timestamp predictionDate;

    @Column(name = "confidence")
    private Float confidence;

    @Column(name = "direction")
    private Boolean direction;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    @Column(name = "actual_result")
    private Boolean actualResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
