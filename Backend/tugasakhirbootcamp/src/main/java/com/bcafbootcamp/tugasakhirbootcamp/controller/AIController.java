package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/api/predict-risk")
    public ResponseEntity<String> getRiskPrediction() {
        try {
            // Mengupdate data risiko di database
            aiService.updateRiskPrediction();
            return ResponseEntity.ok("Risk data updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while updating risk data: " + e.getMessage());
        }
    }
}
