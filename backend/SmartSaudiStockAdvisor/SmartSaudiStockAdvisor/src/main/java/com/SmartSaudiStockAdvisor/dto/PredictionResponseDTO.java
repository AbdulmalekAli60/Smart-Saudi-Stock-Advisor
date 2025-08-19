package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Prediction;

import java.sql.Timestamp;

public class PredictionResponseDTO {
    private Long predictionId;
    private Timestamp predictionDate;
    private float prediction;
    private boolean direction;
    private Timestamp expirationDate;
    private Float actualResult;
    private Long CompanyId;

    public PredictionResponseDTO() {
    }

    public PredictionResponseDTO(Prediction prediction) {
        this.predictionId = prediction.getPredictionId();
        this.predictionDate = prediction.getPredictionDate();
        this.prediction = prediction.getPrediction();
        this.direction = prediction.getDirection();
        this.expirationDate = prediction.getExpirationDate();
        this.actualResult = prediction.getActualResult();
        CompanyId = prediction.getCompany().getCompanyId();
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

    public float getPrediction() {
        return prediction;
    }

    public void setPrediction(float prediction) {
        this.prediction = prediction;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
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

    public Long getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(Long companyId) {
        CompanyId = companyId;
    }
}
