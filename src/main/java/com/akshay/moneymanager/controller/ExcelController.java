package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.service.ExcelService;
import com.akshay.moneymanager.service.ExpenseService;
import com.akshay.moneymanager.service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @GetMapping("/download/income")
    public void downloadIncomeExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.open-xmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=income.xlsx");
        excelService.writeIncomeToExcel(response.getOutputStream(),(List<IncomeDTO>) incomeService.getCurrentMonthIncomesForCurrentUser().getData());
    }

    @GetMapping("/download/expense")
    public void downloadExpenseExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.open-xmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=expense.xlsx");
        excelService.writeExpensesToExcel(response.getOutputStream(),(List<ExpenseDTO>)expenseService.getCurrentMonthExpensesForCurrentUser().getData());
    }
}
