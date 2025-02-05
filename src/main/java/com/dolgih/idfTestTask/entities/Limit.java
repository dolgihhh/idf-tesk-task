package com.dolgih.idfTestTask.entities;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "Limits")
@Table(name = "limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category", nullable = false, length = 20)
    private ExpenseCategory expenseCategory;

    @Column(name = "amount_usd", precision = 19, scale = 2, nullable = false)
    private BigDecimal amountUSD;

    @Builder.Default
    @Column(name = "date_set", nullable = false)
    private LocalDate dateSet = LocalDate.now();

    @OneToMany(mappedBy = "limit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
