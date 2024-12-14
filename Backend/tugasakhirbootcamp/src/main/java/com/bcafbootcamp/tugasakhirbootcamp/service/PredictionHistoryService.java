package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionHistory;
import com.bcafbootcamp.tugasakhirbootcamp.repo.PredictionHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PredictionHistoryService {

    @Autowired
    private PredictionHistoryRepo predictionHistoryRepo;

    // Log awal dari keseluruhan proses prediksi
    public PredictionHistory logPredictionHistory(int totalBatchSize, String status) {
        PredictionHistory predictionHistory = new PredictionHistory();
        predictionHistory.setStartTime(LocalDateTime.now());
        predictionHistory.setEndTime(null); // end time akan diupdate saat selesai
        predictionHistory.setBatchSize(totalBatchSize);
        predictionHistory.setStatus(status);
        return predictionHistoryRepo.save(predictionHistory);
    }

    // Update waktu selesai dan status dari keseluruhan proses prediksi
    public void updatePredictionHistory(Long predictionId, String status) {
        PredictionHistory predictionHistory = predictionHistoryRepo.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Prediction history not found"));

        predictionHistory.setEndTime(LocalDateTime.now());
        predictionHistory.setStatus(status);
        predictionHistoryRepo.save(predictionHistory);
    }

    // Update status jika ada kegagalan pada batch tertentu
    public void markAsFailed(Long predictionId) {
        PredictionHistory predictionHistory = predictionHistoryRepo.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Prediction history not found"));

        predictionHistory.setStatus("Failed");
        predictionHistory.setEndTime(LocalDateTime.now());
        predictionHistoryRepo.save(predictionHistory);
    }
}
