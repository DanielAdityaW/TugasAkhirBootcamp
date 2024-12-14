package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImportExcelController {

    @Autowired
    private ImportExcelService importExcelService;

    @PostMapping("/import-excel")
    public String importExcelData(@RequestParam("file") MultipartFile file) {
        try {
            importExcelService.importExcelData(file);
            return "File imported successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
