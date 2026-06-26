package com.akshay.moneymanager.service;

import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity profileEntity = profileRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email : "+email));
        return User.builder()
                .username(profileEntity.getEmail())
                .password(profileEntity.getPassword())
                .build();
    }
}
