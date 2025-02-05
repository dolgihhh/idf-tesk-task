package com.dolgih.idfTestTask.dtos.responses;

import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionLimitResponseDTO {

    @Schema(description = "Номер счета отправителя", example = "3465373")
    @JsonProperty("account_from")
    private Integer accountFrom;

    @Schema(description = "Номер счета получателя", example = "7653116")
    @JsonProperty("account_to")
    private Integer accountTo;

    @Schema(description = "Валюта транзакции", allowableValues = {"RUB", "KZT"})
    @JsonProperty("currency_shortname")
    private String currencyShortName;

    @Schema(description = "Сумма транзакции в валюте транзакции", example = "100")
    private BigDecimal sum;

    @Schema(description = "Категория траты транзакции")
    @Enumerated(EnumType.STRING)
    @JsonProperty("expense_category")
    private ExpenseCategory expenseCategory;

    @Schema(description = "Дата и время транзакции", example = "2025-01-27T16:54:39.546+03:00")
    @JsonProperty("date_time")
    private ZonedDateTime dateTime;

    @Schema(description = "Сумма транзакции в USD", example = "55")
    @JsonProperty("sum_usd")
    private BigDecimal sumUSD;

    @Schema(description = "Флаг превышения лимита", allowableValues = {"true", "false"})
    @JsonProperty("limit_exceeded")
    private Boolean limitExceeded;

    @Schema(description = "Сумма лимита в USD", example = "2000")
    @JsonProperty("limit_sum")
    private BigDecimal amountUSD;

    @Schema(description = "Дата установления лимита", example = "2025-01-25")
    @JsonProperty("limit_datetime")
    private LocalDate dateSet;

    @Schema(description = "Валюта лимита", allowableValues = {"USD"})
    @JsonProperty("limit_currency_shortname")
    private String limitCurrencyShortName = "USD";
}
