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

    @Column(name = "prediction")
    private Float prediction;

    @Column(name = "direction")
    private Boolean direction;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    @Column(name = "actual_result")
    private Float actualResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Prediction() {
        this.predictionDate = new Timestamp(System.currentTimeMillis());
    }

    public Prediction(Long predictionId, Float prediction, Boolean direction, Timestamp expirationDate, Float actualResult, Company company) {
        this();
        this.predictionId = predictionId;
        this.prediction = prediction;
        this.direction = direction;
        this.expirationDate = expirationDate;
        this.actualResult = actualResult;
        this.company = company;
    }

    public Long getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(Long predictionId) {
        this.predictionId = predictionId;
    }

    public Timestamp getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(Timestamp predictionDate) {
        this.predictionDate = predictionDate;
    }

    public Float getPrediction() {
        return prediction;
    }

    public void setPrediction(Float prediction) {
        this.prediction = prediction;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Float getActualResult() {
        return actualResult;
    }

    public void setActualResult(Float actualResult) {
        this.actualResult = actualResult;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Prediction{" +
                "predictionId=" + predictionId +
                ", predictionDate=" + predictionDate +
                ", confidence=" + prediction +
                ", direction=" + direction +
                ", expirationDate=" + expirationDate +
                ", actualResult=" + actualResult +
                ", company=" + company +
                '}';
    }
}
