package com.akshay.moneymanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class IncomeEntity extends BaseEntity    {

    @Column(name = "income_name", nullable = false, length = 100)
    private String name;

    @Column(name = "icon", length = 1)
    private String icon;

    @Column(name = "expense_date", nullable = false, length = 20)
    private LocalDate date;

    @Column(name = "amount", nullable = false, length = 20)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;

}
