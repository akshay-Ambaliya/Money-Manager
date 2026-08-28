package com.akshay.moneymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<String> healthEndpoint(){
        return ResponseEntity.ok("Up");
    }
    

}
