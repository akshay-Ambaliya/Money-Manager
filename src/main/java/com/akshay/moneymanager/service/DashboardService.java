package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.dto.RecentTransactionDTO;
import com.akshay.moneymanager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
@Service
public class DashboardService {

    private final ProfileService profileService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public ApiResponse getDashboardData(){
        ProfileEntity profile =  profileService.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncome = incomeService.getLatest5IncomesForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransaction=  concat(latestIncome.stream().map(income->
                RecentTransactionDTO.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .type("Income")
                .build()),
                latestExpenses.stream().map(expense->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId())
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("Expense")
                                .build())
                        .sorted((a,b)->{
                            int cmp=b.getDate().compareTo(a.getDate());
                            if(cmp==0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                                return b.getCreatedAt().compareTo(a.getCreatedAt());
                            }
                            return cmp;
                        })
                ).toList();

        returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpenseForCurrentUser()));
        returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recent5Incomes",latestIncome);
        returnValue.put("recentTransactions",recentTransaction);
        return ApiResponse.builder()
                .data(returnValue)
                .success(true)
                .message("Dashboard Details Fetched")
                .status(HttpStatus.OK)
                .time(LocalDateTime.now())
                .build();
    }

}
