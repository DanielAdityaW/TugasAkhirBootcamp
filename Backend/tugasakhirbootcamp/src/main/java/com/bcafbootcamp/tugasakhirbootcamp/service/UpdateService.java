package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.repo.CustomerRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UpdateService {

    @Autowired
    private CustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void updateCustomerData(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Map<String, Customer> customerUpdates = new HashMap<>();
        List<String> customerIds = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            String customerId = getCellValueAsString(row.getCell(0));
            customerIds.add(customerId);

            String riskScore = getCellValueAsString(row.getCell(8));
            String riskCluster = getCellValueAsString(row.getCell(9));

            // Create or update the customer record to be updated
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            customer.setRiskScore(riskScore);
            customer.setRiskCluster(riskCluster);

            customerUpdates.put(customerId, customer);
        }

        List<Customer> existingCustomers = customerRepo.findAllById(customerIds);

        for (Customer existingCustomer : existingCustomers) {
            Customer updateData = customerUpdates.get(existingCustomer.getCustomerId());
            if (updateData != null) {
                existingCustomer.setRiskScore(updateData.getRiskScore());
                existingCustomer.setRiskCluster(updateData.getRiskCluster());
            }
        }

        customerRepo.saveAll(existingCustomers);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}