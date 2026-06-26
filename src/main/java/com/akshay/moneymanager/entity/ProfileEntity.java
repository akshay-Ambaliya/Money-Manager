package com.akshay.moneymanager.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileEntity extends BaseEntity{

    private String fullName;

    private String email;

    private String password;

    private String imageUrl;

    private Boolean isActive;

    private String activationToken;
}


