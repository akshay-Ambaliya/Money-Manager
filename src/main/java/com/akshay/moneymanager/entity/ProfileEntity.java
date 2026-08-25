package com.akshay.moneymanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl;

    private Boolean isActive;

    private String activationToken;
}


