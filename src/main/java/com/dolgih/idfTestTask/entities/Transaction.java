package com.dolgih.idfTestTask.entities;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(name = "Transactions")
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_from", nullable = false)
    private Integer accountFrom;

    @Column(name = "account_to", nullable = false)
    private Integer accountTo;

    @Column(name = "currency_shortname", nullable = false)
    private String currencyShortName;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal sum;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category", nullable = false, length = 20)
    private ExpenseCategory expenseCategory;

    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @Column(name = "sum_usd", precision = 19, scale = 2, nullable = false)
    private BigDecimal sumUSD;

    @Column(name = "limit_exceeded", nullable = false)
    private Boolean limitExceeded;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_id")
    private Limit limit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_rate_id")
    private ExchangeRate exchangeRate;
}
