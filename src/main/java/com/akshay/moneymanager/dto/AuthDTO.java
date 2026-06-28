package com.akshay.moneymanager.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuthDTO {
    private String email;
    private String password;
    private String token;
}
