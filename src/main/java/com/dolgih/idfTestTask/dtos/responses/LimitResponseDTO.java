package com.dolgih.idfTestTask.dtos.responses;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitResponseDTO {

    @Schema(description = "Уникальный идентификатор лимита", example = "15")
    private Integer id;

    @Schema(description = "Категория трат")
    @Enumerated(EnumType.STRING)
    @JsonProperty("expense_category")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Сумма лимита в USD", example = "2500")
    @JsonProperty("amount_usd")
    private BigDecimal amountUSD;

    @Schema(description = "Дата установления лимита", example = "2025-01-25")
    @JsonProperty("date_set")
    private LocalDate dateSet;
}
