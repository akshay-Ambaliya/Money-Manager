package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.service.ExpenseService;
import com.akshay.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse> addIncome(@RequestBody ExpenseDTO dto){
        ApiResponse response =  expenseService.addExpense(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getExpenses(){
        ApiResponse response = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<ApiResponse> deleteExpense(
            @PathVariable Long id
    ){
        ApiResponse response = expenseService.deleteExpense(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
