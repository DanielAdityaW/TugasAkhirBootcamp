package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionRequest;
import com.bcafbootcamp.tugasakhirbootcamp.service.UpdateAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/risk")
public class UpdateAIController {

    @Autowired
    private UpdateAIService updateAIService;

    @PostMapping("/update")
    public String updateRiskData(@RequestBody List<PredictionRequest> predictionRequestList){
        try{
            for (PredictionRequest predictionRequest : predictionRequestList){
                updateAIService.updateRiskPrediction(predictionRequest);
            }
            return "update success";
        } catch (Exception e){
            return "error occurred:" + e.getMessage();
        }
    }
}
