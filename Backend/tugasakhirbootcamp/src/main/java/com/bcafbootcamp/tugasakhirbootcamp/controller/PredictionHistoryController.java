package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionHistory;
import com.bcafbootcamp.tugasakhirbootcamp.repo.PredictionHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:4200")
public class PredictionHistoryController {

    @Autowired
    private PredictionHistoryRepo predictionHistoryRepo;

    @GetMapping
    public List<PredictionHistory> getPredictionHistory() {
        return predictionHistoryRepo.findTop30ByOrderByIdDesc();
    }
}
