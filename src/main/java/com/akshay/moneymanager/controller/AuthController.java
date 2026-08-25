package com.akshay.moneymanager.controller;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.AuthDTO;
import com.akshay.moneymanager.dto.ProfileDTO;
import com.akshay.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
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
        log.info("Entered");
        ApiResponse response = profileService.authenticateAndGenerateToken(authDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentProfileDetail(){
        ApiResponse response = profileService.getCurrentProfileDetail();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody ProfileDTO profileDTO){
        ApiResponse response = profileService.updateProfile(profileDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
