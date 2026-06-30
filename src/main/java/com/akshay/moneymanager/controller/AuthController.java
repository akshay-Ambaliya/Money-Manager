package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.AuthDTO;
import com.akshay.moneymanager.dto.ProfileDTO;
import com.akshay.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ApiResponse registerUser(
            @RequestBody ProfileDTO profileDto
    ){
        return profileService.registerUser(profileDto);
    }

    @GetMapping("profile/activate")
    public ResponseEntity<ApiResponse> activateToken(@RequestParam String token){
        ApiResponse response = profileService.activeToken(token);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthDTO authDTO){
        ApiResponse response = profileService.authenticateAndGenerateToken(authDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
