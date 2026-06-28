package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ApiResponse;
import com.akshay.moneymanager.dto.AuthDTO;
import com.akshay.moneymanager.dto.ProfileDTO;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.repository.ProfileRepository;
import com.akshay.moneymanager.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    public ApiResponse registerUser(ProfileDTO profileDTO){

        if(profileRepository.existsByEmail(profileDTO.getEmail())){
            throw new BadCredentialsException("User already exist with the email ID : "+profileDTO.getEmail());
        }

        ProfileEntity profile = modelMapper.map(profileDTO, ProfileEntity.class);
        profile.setActivationToken(UUID.randomUUID().toString());
        profile.setIsActive(false);
        profile.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        profileRepository.save(profile);

        String activationLink = "http://localhost:8081/api/v1/auth/profile/activate?token="+profile.getActivationToken();
        String subject = "Activate your money manager account";
        String body = "Click on the following link to activate your account : "+activationLink;

        emailService.sendEmail(profile.getEmail(),subject,body);


        return new ApiResponse(
                HttpStatus.CREATED,
                ProfileDTO.builder()
                        .email(profile.getEmail())
                        .fullName(profile.getFullName())
                        .activationToken(profile.getActivationToken())
                        .build(),
                LocalDateTime.now(),true,"Successfully email sent");
    }

    public ApiResponse activeToken(String token) {
        Optional<ProfileEntity> profile = profileRepository.findByActivationToken(token);
        if(profile.isPresent()){
            profile.get().setIsActive(true);
            profileRepository.save(profile.get());
            return new ApiResponse(HttpStatus.OK,null,LocalDateTime.now(),true,"Profile Has Been Activated Successfully");
        }else{
            throw new BadCredentialsException("Email not found");
        }
    }

    public boolean isAccountActive(String email){
        ProfileEntity profileEntity = profileRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email : "+email));

        return profileEntity.getIsActive();
    }

    public ApiResponse authenticateAndGenerateToken(AuthDTO authDTO) {

        if (!isAccountActive(authDTO.getEmail())) {
            throw new BadCredentialsException("Provided User profile is not active");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getEmail(),
                            authDTO.getPassword()
                    )
            );

            String token = jwtUtils.generateToken(authDTO.getEmail());

            HashMap<String,Object> response = new HashMap<>();
            response.put("email",authDTO.getEmail());
            response.put("token",token);

            return new ApiResponse(
                    HttpStatus.OK,
                    response,
                    LocalDateTime.now(),
                    true,
                    "Token Generated Successully"
            );

        } catch (BadCredentialsException ex) {
            return new ApiResponse(
                    HttpStatus.UNAUTHORIZED,
                    null,
                    LocalDateTime.now(),
                    true,
                    "Invalid email or password"
            );
        }
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(()-> new UsernameNotFoundException("Profile not found with email : "+authentication.getName()));

    }

    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentUser= null;
        if(email == null){
            currentUser = getCurrentProfile();
        }else{
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(() ->  new UsernameNotFoundException("Profile not found with email : "+ email));
        }

        return ProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(currentUser.getImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdateAt())
                .build();
    }
}
