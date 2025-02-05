package com.dolgih.idfTestTask.dtos.requests;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание лимита")
public class LimitRequestDTO {

    @Schema(description = "Категория трат")
    @Enumerated(EnumType.STRING)
    @JsonProperty("expense_category")
    @NotNull(message = "Expense category type type cannot be null")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Сумма лимита в USD", example = "2500")
    @DecimalMin(value = "0", message = "Amount USD must be greater than or equal to 0")
    @NotNull(message = "Amount USD cannot be null")
    @JsonProperty("amount_usd")
    private BigDecimal amountUSD;
}
