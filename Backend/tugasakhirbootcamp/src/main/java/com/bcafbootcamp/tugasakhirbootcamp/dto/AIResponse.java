package com.bcafbootcamp.tugasakhirbootcamp.dto;

import java.util.List;

public class AIResponse {
    private List<String> customerID;
    private List<String> predictedRisk;
    private List<String> predictedRiskScore;

    public List<String> getCustomerID() {
        return customerID;
    }

    public void setCustomerID(List<String> customerID) {
        this.customerID = customerID;
    }

    // Getters and Setters
    public List<String> getPredictedRisk() {
        return predictedRisk;
    }

    public void setPredictedRisk(List<String> predictedRisk) {
        this.predictedRisk = predictedRisk;
    }

    public List<String> getPredictedRiskScore() {
        return predictedRiskScore;
    }

    public void setPredictedRiskScore(List<String> predictedRiskScore) {
        this.predictedRiskScore = predictedRiskScore;
    }
}
