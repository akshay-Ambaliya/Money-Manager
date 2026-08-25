package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse> getDashboardDetails(){
        ApiResponse response = dashboardService.getDashboardData();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
