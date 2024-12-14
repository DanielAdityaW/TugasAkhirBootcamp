package com.bcafbootcamp.tugasakhirbootcamp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ManualInputAIController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/predict-manual")
    public ResponseEntity<Map<String, Object>> predictManual(@RequestBody Map<String, Object> input) {
        String flaskUrl = "http://localhost:5000/manual-predict";
        try {
            // Send POST request to Flask service
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, input, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getBody());
            }

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();  // Changed to Map<String, Object>
            errorResponse.put("error", "Failed to get prediction from Flask service");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
