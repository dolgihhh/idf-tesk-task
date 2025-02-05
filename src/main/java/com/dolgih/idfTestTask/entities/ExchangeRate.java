package com.dolgih.idfTestTask.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "ExchangeRates")
@Table(name = "exchange_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Builder.Default
    @Column(name = "date", nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(nullable = false, length = 15)
    private String currencyPair;

    @Column(precision = 19, scale = 6, nullable = false)
    private BigDecimal rate;

    @OneToMany(mappedBy = "exchangeRate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
