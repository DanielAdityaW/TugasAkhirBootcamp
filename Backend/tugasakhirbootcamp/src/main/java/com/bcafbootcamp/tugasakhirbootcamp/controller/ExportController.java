package com.bcafbootcamp.tugasakhirbootcamp.controller;

import com.bcafbootcamp.tugasakhirbootcamp.service.ExportExcelService;
import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ExportController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ExportExcelService exportExcelService;

    @GetMapping("/export-customer-data")
    public void exportCustomerData(HttpServletResponse response) throws IOException{
        List<Customer> customerList = customerService.getAllCustomerData();
        exportExcelService.export(customerList, response);
    }
}
