package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.IncomeDTO;
import com.akshay.moneymanager.entity.CategoryEntity;
import com.akshay.moneymanager.entity.IncomeEntity;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.exception.ResourceNotFoundException;
import com.akshay.moneymanager.repository.CategoryRepository;
import com.akshay.moneymanager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    public ApiResponse addIncome(IncomeDTO income){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(income.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category Not Found with id : "+income.getCategoryId()));

        if(income.getDate() == null){
            income.setDate(LocalDate.now());
        }

        IncomeEntity newExpense= toEntity(income, profile, category);
        newExpense = incomeRepository.save(newExpense);
        return ApiResponse.builder()
                .message("Income created Successfully")
                .data(newExpense)
                .status(HttpStatus.CREATED)
                .success(true)
                .time(LocalDateTime.now())
                .build();
    }

    // Retrieve all the incomes for current month/based on the startDate and endDate
    public ApiResponse getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);

        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(list.stream().map(this::toDTO).toList())
                .message("Fetched Successfully")
                .success(true)
                .build();
    }

    // Delete expense by id for current user
    public ApiResponse deleteIncome(Long incomeId){
        ProfileEntity profile =  profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository. findById(incomeId)
                .orElseThrow(()-> new ResourceNotFoundException("Income Not found with id : "+incomeId));

        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this income");
        }

        incomeRepository.delete(entity);

        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(null)
                .message("Deleted Successfully")
                .success(true)
                .build();
    }

    // Get current 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    // Get Total incomes for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }

    // Helper Methods
    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profileEntity, CategoryEntity category){

        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profileEntity)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity)
    {
        return IncomeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null ? entity.getCategory().getId(): null)
                .name(entity.getCategory()!=null ? entity.getCategory().getName(): "N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }
}
