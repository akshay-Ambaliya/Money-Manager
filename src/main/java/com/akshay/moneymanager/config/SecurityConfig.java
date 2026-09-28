package com.akshay.moneymanager.config;

import com.akshay.moneymanager.filter.JWTfilter;
import com.akshay.moneymanager.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService userDetailService;
    private final JWTfilter jwTfilter;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;
    @Bean
    public SecurityFilterChain createSecurityFilterChain(HttpSecurity http){
        http.
                // Spring Security enables CSRF protection by default because it's important for applications that use session-based authentication (cookies).
                // We need to disable for stateless REST-APIs that uses JWT tokens
                csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to access the login/register/activate/health endpoints without authentication
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/profile/activate").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Every other request must be fully authenticated
                        .anyRequest().authenticated()
                )
                // explicitly telling Spring Security never to create or use an HttpSession
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwTfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // our frontend and backend both will run on different port number, so for the backend request if coming from another origin
    // How our request should behave that we need to configure
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        if (frontendUrl != null && !frontendUrl.trim().isEmpty() && !frontendUrl.equals("http://localhost:5173")) {
            configuration.setAllowedOrigins(List.of("http://localhost:5173", frontendUrl));
        } else {
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        }
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // if you use cookies; otherwise false is also fine

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder createPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager createAuthenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
        provider.setPasswordEncoder(createPasswordEncoder());
        return new ProviderManager(provider);

    }
}
