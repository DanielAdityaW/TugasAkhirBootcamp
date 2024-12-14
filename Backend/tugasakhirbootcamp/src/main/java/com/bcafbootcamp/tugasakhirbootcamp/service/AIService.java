package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.dto.AIResponse;
import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.model.PredictionHistory;
import com.bcafbootcamp.tugasakhirbootcamp.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);  // Logger

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PredictionHistoryService predictionHistoryService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    private static final int BATCH_SIZE = 5000; // Ukuran batch yang lebih besar

    @Transactional
    @Scheduled(cron = "0 */3 * * * *") // Menjalankan setiap 3 menit (180000 ms)
    public void updateRiskPrediction() {
        int pageNumber   = 0;
        boolean isLastBatch = false;

        // Log keseluruhan proses
        PredictionHistory predictionHistory = predictionHistoryService.logPredictionHistory(10000, "In Progress");

        while (!isLastBatch) {
            List<Customer> customers = customerRepo.findAll(PageRequest.of(pageNumber, BATCH_SIZE)).getContent();
            if (customers.isEmpty()) break;

            // Cek apakah ini batch terakhir
            isLastBatch = customers.size() < BATCH_SIZE;

            updateRiskPredictionAsync(customers, predictionHistory);
            pageNumber++;
        }

        // Perbarui log prediction history jika semua batch telah selesai
        predictionHistoryService.updatePredictionHistory(predictionHistory.getId(), "Completed");
        logger.info("Keseluruhan proses update batch selesai untuk semua pelanggan.");
    }

    @Transactional
    @Async
    public CompletableFuture<Void> updateRiskPredictionAsync(List<Customer> customers, PredictionHistory predictionHistory) {
        // Siapkan data dalam format yang diinginkan oleh Flask
        List<Map<String, String>> customerData = customers.stream().map(customer -> {
            return Map.of(
                    "Customer ID", customer.getCustomerId(),
                    "Vehicle Type", customer.getVehicleType(),
                    "Payment History", customer.getPaymentHistory(),
                    "Credit Score", customer.getCreditScore(),
                    "Tenure", customer.getTenure(),
                    "Age", customer.getAge(),
                    "Balance", customer.getBalance(),
                    "Installment Amount", customer.getInstallmentAmount()
            );
        }).collect(Collectors.toList());

        try {
            // Kirim data ke Flask untuk mendapatkan prediksi
            ResponseEntity<AIResponse> response = restTemplate.postForEntity(pythonApiUrl + "/predict", customerData, AIResponse.class);

            // Pastikan Flask mengembalikan respons yang valid
            if (response.getStatusCode().is2xxSuccessful()) {
                AIResponse aiResponse = response.getBody();
                for (int i = 0; i < customers.size(); i++) {
                    Customer customer = customers.get(i);
                    String riskScore = aiResponse.getPredictedRiskScore().get(i);
                    String riskCluster = aiResponse.getPredictedRisk().get(i);
                    customer.setRiskScore(riskScore);
                    customer.setRiskCluster(riskCluster);
                }

                // Simpan perubahan secara batch
                customerRepo.saveAll(customers);

                // Log informasi bahwa update berhasil untuk batch ini
                logger.info("Batch update selesai untuk {} pelanggan.", customers.size());

            } else {
                // Log error dan perbarui status menjadi 'failed'
                logger.error("Flask API call failed with status: {}", response.getStatusCode());

                // Update status prediction history menjadi 'failed' jika ada kegagalan pada batch ini
                predictionHistoryService.updatePredictionHistory(predictionHistory.getId(), "Failed");

                // Lempar exception setelah mencatat kegagalan
                throw new RuntimeException("Flask API call failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // Tangani exception yang terjadi selama proses prediksi
            logger.error("Error during prediction: {}", e.getMessage(), e);

            // Update status prediction history menjadi 'failed' jika ada exception
            predictionHistoryService.updatePredictionHistory(predictionHistory.getId(), "Failed");

            // Lempar exception agar error dapat terpropagasi
            throw new RuntimeException("Error during prediction: " + e.getMessage(), e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
