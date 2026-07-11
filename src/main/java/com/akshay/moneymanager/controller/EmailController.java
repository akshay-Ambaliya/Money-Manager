package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final EmailService emailService;
    private final ProfileService profileService;

    @GetMapping("/income-excel")
    public ResponseEntity<Void> emailIncomeExcel() throws Exception{

        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeIncomeToExcel(baos,(List<IncomeDTO>)incomeService.getCurrentMonthIncomesForCurrentUser().getData());
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Income Excel Report ",
                "Please find attached your income report",
                baos.toByteArray(),
                "income.xlsx");
        return ResponseEntity.ok(null);
    }

    @GetMapping("/expense-excel")
    public ResponseEntity<Void> emailExpenseExcel() throws Exception{
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(baos,(List<ExpenseDTO>)expenseService.getCurrentMonthExpensesForCurrentUser().getData());
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Expense Excel Report ",
                "Please find attached your expense report",
                baos.toByteArray(),
                "expense.xlsx");
        return ResponseEntity.ok(null);
    }
}
