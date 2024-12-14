package com.bcafbootcamp.tugasakhirbootcamp.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;

@Service
public class PythonScriptService {

    private final String scriptPath = "src/main/resources/Machine-Learning/app.py";

    @PostConstruct
    public void runPythonScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", scriptPath);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
