package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.service.ExpenseService;
import com.akshay.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ExpenseDTO> addIncome(@RequestBody ExpenseDTO dto){
        ExpenseDTO saved =  expenseService.addExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses(){
        List<ExpenseDTO> expenses = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id
    ){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
