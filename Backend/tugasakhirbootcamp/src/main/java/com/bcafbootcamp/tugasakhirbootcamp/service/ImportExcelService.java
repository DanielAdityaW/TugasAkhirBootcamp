package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import com.bcafbootcamp.tugasakhirbootcamp.repo.CustomerRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ImportExcelService {

    @Autowired
    private CustomerRepo customerRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void importExcelData(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            List<Customer> customers = new ArrayList<>();
            int batchSize = 1000;
            int count = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String customerId = getCellValueAsString(row.getCell(0));
                String creditScore = getCellValueAsString(row.getCell(1));
                String age = getCellValueAsString(row.getCell(2));
                String tenure = getCellValueAsString(row.getCell(3));
                String balance = getCellValueAsString(row.getCell(4));
                String vehicleType = getCellValueAsString(row.getCell(5));
                String installmentAmount = getCellValueAsString(row.getCell(6));
                String paymentHistory = getCellValueAsString(row.getCell(7));
                String riskScore = getCellValueAsString(row.getCell(8));
                String riskCluster = getCellValueAsString(row.getCell(9));

                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                customer.setCreditScore(creditScore);
                customer.setBalance(balance);
                customer.setAge(age);
                customer.setTenure(tenure);
                customer.setInstallmentAmount(installmentAmount);
                customer.setVehicleType(vehicleType);
                customer.setPaymentHistory(paymentHistory);
                customer.setRiskScore(riskScore);
                customer.setRiskCluster(riskCluster);

                customers.add(customer);

                if (++count % batchSize == 0) {
                    saveBatch(customers);
                    customers.clear();
                }
            }

            if (!customers.isEmpty()) {
                saveBatch(customers);
            }
        }
    }

    @Transactional
    private void saveBatch(List<Customer> customers) {
        for (int i = 0; i < customers.size(); i++) {
            entityManager.persist(customers.get(i));
            if (i % 50 == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
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