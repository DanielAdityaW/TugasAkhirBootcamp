package com.bcafbootcamp.tugasakhirbootcamp.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExcelUploadAIController {

    private final String flaskUrl = "http://localhost:5000/predict-excel";  // URL Flask
    private byte[] processedFileBytes;

    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Check if the file is empty
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "error", "message", "No file selected for upload."));
            }

            // Send file to Flask for processing
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Resource> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, Resource.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Read the byte content of the resource
                processedFileBytes = readResourceToByteArray(response.getBody());
                System.out.println("Processed file bytes successfully loaded."); // Log message for verification
                return ResponseEntity.ok(Map.of("status", "success", "message", "File processed successfully", "downloadUrl", "/api/download-predicted-results"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("status", "error", "message", "Error processing file."));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Error reading the file."));
        }
    }

    @GetMapping("/download-predicted-results")
    public ResponseEntity<byte[]> downloadProcessedFile() {
        try {
            if (processedFileBytes == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            // Return the processed file as a downloadable response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=predicted_results.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(processedFileBytes);
        } catch (Exception e) {
            // Log any unexpected error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Helper method to read bytes from the Resource
    private byte[] readResourceToByteArray(Resource resource) throws IOException {
        return resource.getInputStream().readAllBytes();
    }
}
