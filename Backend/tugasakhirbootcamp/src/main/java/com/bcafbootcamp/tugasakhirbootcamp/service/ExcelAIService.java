package com.bcafbootcamp.tugasakhirbootcamp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import java.io.IOException;

@Service
public class ExcelAIService {

    @Value("${python.api.url}")
    private String flaskUrl; // URL Flask service (e.g., http://localhost:5000)

    private final RestTemplate restTemplate;

    public ExcelAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Resource processAndPredict(MultipartFile file) throws IOException {
        // Prepare the file to be sent to Flask
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource()); // Add file to the body

        // Prepare headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create HTTP entity with file and headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the POST request to Flask API
        ResponseEntity<byte[]> response = restTemplate.exchange(
                flaskUrl + "/predict-excel",
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        // Return the byte[] received from Flask (Excel file)
        return new ByteArrayResource(response.getBody());
    }
}
