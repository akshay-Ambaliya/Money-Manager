package com.akshay.moneymanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
