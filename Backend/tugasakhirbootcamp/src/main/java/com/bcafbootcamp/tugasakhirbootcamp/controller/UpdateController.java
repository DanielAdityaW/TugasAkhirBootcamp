package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @PostMapping("/update-data")
    public ResponseEntity<String> uploadCustomerFile(@RequestParam("file") MultipartFile file){
        try {
            updateService.updateCustomerData(file);
            return ResponseEntity.ok("Data berhasil diperbaharui");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Terjadi Kesalahan saat upload file.");
        }
    }
}
