package com.bcafbootcamp.tugasakhirbootcamp.model;

public class PredictionRequest {
    private String customerId;
    private String predictedRiskScore;
    private String predictedRisk;

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPredictedRiskScore() {
        return predictedRiskScore;
    }

    public void setPredictedRiskScore(String predictedRiskScore) {
        this.predictedRiskScore = predictedRiskScore;
    }

    public String getPredictedRisk() {
        return predictedRisk;
    }

    public void setPredictedRisk(String predictedRisk) {
        this.predictedRisk = predictedRisk;
    }
}

