package com.bcafbootcamp.tugasakhirbootcamp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_data")
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    @Column(name = "credit_score", nullable = true)
    private String creditScore;
    @Column(name = "age", nullable = true)
    private String age;
    @Column(name = "tenure", nullable = true)
    private String tenure;
    @Column(name = "balance", nullable = true)
    private String balance;
    @Column(name = "vehicle_type", nullable = true)
        private String vehicleType;
    @Column(name = "installment_amount", nullable = true)
    private String installmentAmount;
    @Column(name = "payment_history", nullable = true)
    private String paymentHistory;
    @Column(name="risk_score", nullable = true)
    private String riskScore;
    @Column(name="risk_cluster", nullable = true)
    private String riskCluster;

    private boolean isNunggak;

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreditScore() {
        return creditScore;
    }
    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getTenure(){
        return tenure;
    }
    public void setTenure(String tenure){
        this.tenure = tenure;
    }

    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }
    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getPaymentHistory() {
        return paymentHistory;
    }
    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public String getRiskScore() {
        return riskScore;
    }
    public void setRiskScore(String riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskCluster() {
        return riskCluster;
    }
    public void setRiskCluster(String riskCluster) {
        this.riskCluster = riskCluster;
    }

    public boolean isNunggak() {
        return isNunggak;
    }
    public void setNunggak(boolean nunggak) {
        isNunggak = nunggak;
    }
}
