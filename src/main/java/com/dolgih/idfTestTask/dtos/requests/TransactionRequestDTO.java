package com.dolgih.idfTestTask.dtos.requests;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание транзакции")
public class TransactionRequestDTO {

    @Schema(description = "Номер счета отправителя", example = "3465373")
    @Min(value = 1, message = "account_from must be greater than or equal to 1")
    @NotNull(message = "Account From cannot be null")
    @JsonProperty("account_from")
    private Integer accountFrom;

    @Schema(description = "Номер счета получателя", example = "7653116")
    @Min(value = 1, message = "account_to must be greater than or equal to 1")
    @NotNull(message = "Account To cannot be null")
    @JsonProperty("account_to")
    private Integer accountTo;

    @Schema(description = "Валюта транзакции", allowableValues = {"RUB", "KZT"})
    @NotBlank(message = "Currency shortname is required")
    @JsonProperty("currency_shortname")
    private String currencyShortName;

    @Schema(description = "Сумма транзакции", example = "100")
    @DecimalMin(value = "0.01", message = "Sum must be greater than or equal to 0.01")
    @NotNull(message = "Sum cannot be null")
    private BigDecimal sum;

    @Schema(description = "Категория траты транзакции")
    @NotNull(message = "Expense category type type cannot be null")
    @Enumerated(EnumType.STRING)
    @JsonProperty("expense_category")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Дата и время транзакции", example = "2025-01-27T16:54:39.546+03:00")
    @NotNull(message = "Date type type cannot be null")
    @JsonProperty("date_time")
    private ZonedDateTime dateTime;
}
