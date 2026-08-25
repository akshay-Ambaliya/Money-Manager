package com.akshay.moneymanager.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long id;
    private String name;
    private String icon;
    private LocalDate date;
    private BigDecimal amount;
    private Long categoryId;
    private Long profileId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
