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
