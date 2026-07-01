package com.akshay.moneymanager.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {

    private Long id;
    private String name;
    private String icon;
    private LocalDate date;
    private String amount;
    private Long categoryId;
    private Long profileId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
