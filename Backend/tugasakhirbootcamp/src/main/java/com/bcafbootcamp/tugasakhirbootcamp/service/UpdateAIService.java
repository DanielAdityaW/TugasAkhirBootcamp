package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionRequest;
import com.bcafbootcamp.tugasakhirbootcamp.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAIService {

    @Autowired
    private CustomerRepo customerRepo;

    public void updateRiskPrediction(PredictionRequest predictionRequest) {
        // Dapatkan customer berdasarkan customerId
        Customer customer = customerRepo.findById(predictionRequest.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));

        // Update Risk Score dan Risk Cluster
        customer.setRiskScore(predictionRequest.getPredictedRiskScore());
        customer.setRiskCluster(predictionRequest.getPredictedRisk());

        // Simpan perubahan ke database
        customerRepo.save(customer);
    }
}