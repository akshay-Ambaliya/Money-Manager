package com.akshay.moneymanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProfileDTO {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String profileImageUrl;
    private String activationToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
