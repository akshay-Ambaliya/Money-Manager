package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.FilterDTO;
import com.akshay.moneymanager.service.ExpenseService;
import com.akshay.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<ApiResponse> filterTransaction(@RequestBody FilterDTO filterDTO){

        LocalDate startDate = filterDTO.getStartDate()!= null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate()!= null ? filterDTO.getEndDate(): LocalDate.now();
        String keyword = filterDTO.getKeyword()!= null ? filterDTO.getKeyword():"";
        String sortField = filterDTO.getSorField()!=null?filterDTO.getSorField(): "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder())?Sort.Direction.DESC: Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        ApiResponse response;
        if(filterDTO.getType().equals("income")){
            response = incomeService.filterIncomes(startDate,endDate,keyword,sort);
        }else if(filterDTO.getType().equals("expense")){
            response = expenseService.filterExpense(startDate,endDate,keyword,sort);
        }else{
            throw new RuntimeException("Invalid type. must be 'income' or 'expense' ");
        }

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
