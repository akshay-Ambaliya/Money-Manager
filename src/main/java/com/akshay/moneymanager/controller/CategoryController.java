package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.CategoryDTO;
import com.akshay.moneymanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCategory(@RequestBody CategoryDTO categoryDTO){
        ApiResponse response = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCategories(){
        ApiResponse response =  categoryService.getCategoriesForCurrentUser();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse> getCategories(
            @RequestParam String type
    ){
        ApiResponse response =  categoryService.getCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategories(
            @PathVariable Long categoryId,
            @RequestBody CategoryDTO categoryDTO
    ){
        ApiResponse response =  categoryService.updateCategory(categoryId,categoryDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
