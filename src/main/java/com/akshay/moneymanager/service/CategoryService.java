package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.CategoryDTO;
import com.akshay.moneymanager.entity.CategoryEntity;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    // save category
    public ApiResponse saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile = profileService.getCurrentProfile();

        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())){
            throw new RuntimeException("Category with this name already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profile);
        newCategory = categoryRepository.save(newCategory);

        return ApiResponse.builder()
                .data(toDto(newCategory))
                .time(LocalDateTime.now())
                .message("Successfully Created")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
    }

    // get categories for current user
    public ApiResponse getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());

        return ApiResponse.builder()
                .data(categories.stream().map(this::toDto).toList())
                .time(LocalDateTime.now())
                .message("Successfully fetched")
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    // get categories by type for current user
    public ApiResponse getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type,profile.getId());

        return ApiResponse.builder()
                .data(categories.stream().map(this::toDto).toList())
                .time(LocalDateTime.now())
                .message("Successfully fetched")
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    public ApiResponse updateCategory(Long categoryId, CategoryDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
        categoryRepository.findByIdAndProfileId(categoryId,profile.getId());

        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                .orElseThrow(()-> new RuntimeException("Category not found or not accssible"));
        if(dto.getIcon()!=null)
            existingCategory.setIcon(dto.getIcon());

        if(dto.getType()!=null)
            existingCategory.setType(dto.getType());

        if(dto.getName()!=null)
            existingCategory.setName(dto.getName());




        return ApiResponse.builder()
                .data(toDto(categoryRepository.save(existingCategory)))
                .time(LocalDateTime.now())
                .message("Successfully updated")
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    // Helper Methods
    private CategoryEntity toEntity(CategoryDTO dto , ProfileEntity profile){
        return CategoryEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .profile(profile)
                .type(dto.getType())
                .build();
    }

    private CategoryDTO toDto(CategoryEntity entity){
        return CategoryDTO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .name(entity.getName())
                .icon(entity.getIcon())
                .profileId(entity.getProfile()!= null?entity.getProfile().getId():null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }
}
