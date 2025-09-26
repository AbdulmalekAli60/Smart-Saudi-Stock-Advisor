package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.Prediction;

import java.time.LocalDateTime;

public class PredictionResponseDTO {
    private Long predictionId;
    private LocalDateTime predictionDate;
    private float prediction;
    private boolean direction;
    private LocalDateTime expirationDate;
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

    public LocalDateTime getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDateTime predictionDate) {
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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
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
