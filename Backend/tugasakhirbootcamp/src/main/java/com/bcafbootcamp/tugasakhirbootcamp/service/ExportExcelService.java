package com.bcafbootcamp.tugasakhirbootcamp.service;

import com.bcafbootcamp.tugasakhirbootcamp.model.Customer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@Component
public class ExportExcelService {

    public void export(List<Customer> customerDataList, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customer Data");

        Row headerRow = sheet.createRow(0);
        String[] columns = {
                "Customer ID",
                "Credit Score",
                "Age",
                "Tenure",
                "Balance",
                "Vehicle Type",
                "Installment Amount",
                "Payment History"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Customer customer : customerDataList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(customer.getCustomerId());
            row.createCell(1).setCellValue(customer.getCreditScore());
            row.createCell(2).setCellValue(customer.getAge());
            row.createCell(3).setCellValue(customer.getTenure());
            row.createCell(4).setCellValue(customer.getBalance());
            row.createCell(5).setCellValue(customer.getVehicleType());
            row.createCell(6).setCellValue(customer.getInstallmentAmount());
            row.createCell(7).setCellValue(customer.getPaymentHistory());
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=export_customer_data.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }
}
