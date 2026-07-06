package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    @PostMapping
    public ResponseEntity<ApiResponse> addIncome(@RequestBody IncomeDTO dto){
        ApiResponse response = incomeService.addIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getIncomes(){
        ApiResponse response = incomeService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteIncome(
            @PathVariable Long id
    ){
        ApiResponse response = incomeService.deleteIncome(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
