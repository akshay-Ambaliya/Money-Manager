package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.entity.CategoryEntity;
import com.akshay.moneymanager.entity.ExpenseEntity;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.exception.ResourceNotFoundException;
import com.akshay.moneymanager.repository.CategoryRepository;
import com.akshay.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    // Adding a new expense to the Database
    public ExpenseDTO addExpense(ExpenseDTO expense){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(expense.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category Not Found with id : "+expense.getCategoryId()));

        if(expense.getDate() == null){
            expense.setDate(LocalDate.now());
        }

        ExpenseEntity newExpense= toEntity(expense, profile, category);
        newExpense = expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }

    // Retrieve all the incomes for current month/based on the startDate and endDate
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDTO).toList();
    }

    // Delete expense by id for current user
    public void deleteExpense(Long expenseId){
        ProfileEntity profile =  profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository. findById(expenseId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense Not found with id : "+expenseId));

        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepository.delete(entity);
    }
    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profileEntity, CategoryEntity category){

        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profileEntity)
                .category(category)
                .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity entity)
    {
        return ExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null ? entity.getCategory().getId(): null)
                .profileId(entity.getProfile()!=null? entity.getProfile().getId(): null)
                .name(entity.getCategory()!=null ? entity.getCategory().getName(): "N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }


}
