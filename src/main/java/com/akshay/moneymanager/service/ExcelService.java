package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.entity.ExpenseEntity;
import com.akshay.moneymanager.entity.IncomeEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExcelService {
    public void writeIncomeToExcel(OutputStream outputStream, List<IncomeEntity> incomes) throws Exception{
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Incomes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            IntStream.range(0, incomes.size()).forEach(i -> {

                IncomeEntity income = incomes.get(i);

                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(i + 1);

                row.createCell(1).setCellValue(
                        income.getName() != null ? income.getName() : "N/A"
                );

                row.createCell(2).setCellValue(
                        income.getCategory() != null
                                ? String.valueOf(income.getCategory().getId())
                                : "N/A"
                );

                if (income.getAmount() != null) {
                    row.createCell(3).setCellValue(income.getAmount().doubleValue());
                } else {
                    row.createCell(3).setCellValue("N/A");
                }

                row.createCell(4).setCellValue(
                        income.getDate() != null
                                ? income.getDate().toString()
                                : "N/A"
                );
            });

            workbook.write(outputStream);
        }
    }

    public void writeExpensesToExcel(OutputStream outputStream, List<ExpenseEntity> expenses) throws IOException {
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Expenses");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            IntStream.range(0, expenses.size()).forEach(i -> {

                ExpenseEntity expense = expenses.get(i);

                Row row = sheet.createRow(i + 1);

                row.createCell(0).setCellValue(i + 1);

                row.createCell(1).setCellValue(
                        expense.getName() != null ? expense.getName() : "N/A"
                );

                row.createCell(2).setCellValue(
                        expense.getCategory() != null
                                ? String.valueOf(expense.getCategory().getId())
                                : "N/A"
                );

                if (expense.getAmount() != null) {
                    row.createCell(3).setCellValue(expense.getAmount().doubleValue());
                } else {
                    row.createCell(3).setCellValue("N/A");
                }

                row.createCell(4).setCellValue(
                        expense.getDate() != null
                                ? expense.getDate().toString()
                                : "N/A"
                );
            });

            workbook.write(outputStream);
        }
    }
}
