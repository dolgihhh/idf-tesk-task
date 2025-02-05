package com.dolgih.idfTestTask;

import com.dolgih.idfTestTask.entities.Limit;
import com.dolgih.idfTestTask.entities.Transaction;
import com.dolgih.idfTestTask.repositories.TransactionRepository;
import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.dolgih.idfTestTask.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testLimitExceededWithMultipleTransactionsAndLimitChanges() {
        ExpenseCategory category = ExpenseCategory.PRODUCT;

        // Лимит 1: 1000 USD, установлен 1 января 2022 года
        Limit limit1 = new Limit();
        limit1.setAmountUSD(BigDecimal.valueOf(1000));
        limit1.setDateSet(LocalDate.of(2022, 1, 1));

        // Лимит 2: 2000 USD, установлен 10 января 2022 года
        Limit limit2 = new Limit();
        limit2.setAmountUSD(BigDecimal.valueOf(2000));
        limit2.setDateSet(LocalDate.of(2022, 1, 10));

        // Мокируем сумму предыдущих транзакций
        when(transactionRepository.getTotalTransactionsSumPerMonth(2022, 1, category))
                .thenReturn(BigDecimal.ZERO) // На 2 января сумма предыдущих транзакций: 0
                .thenReturn(BigDecimal.valueOf(500)) // На 3 января сумма предыдущих транзакций: 500
                .thenReturn(BigDecimal.valueOf(1100)) // На 11 января сумма предыдущих транзакций: 1100
                .thenReturn(BigDecimal.valueOf(1200)) // На 12 января сумма предыдущих транзакций: 1200
                .thenReturn(BigDecimal.valueOf(1900)) // На 13 января сумма предыдущих транзакций: 1900
                .thenReturn(BigDecimal.valueOf(2000)); // На 13 января сумма предыдущих транзакций: 2000

        // Транзакция 1: 2 января 2022 года, 500 USD
        Transaction transaction1 = new Transaction();
        transaction1.setExpenseCategory(category);
        transaction1.setSumUSD(BigDecimal.valueOf(500));
        transaction1.setLimit(limit1);
        transaction1.setDateTime(ZonedDateTime.parse("2022-01-02T00:00:00Z"));

        // Транзакция 2: 3 января 2022 года, 600 USD
        Transaction transaction2 = new Transaction();
        transaction2.setExpenseCategory(category);
        transaction2.setSumUSD(BigDecimal.valueOf(600));
        transaction2.setLimit(limit1);
        transaction2.setDateTime(ZonedDateTime.parse("2022-01-03T00:00:00Z"));

        // Транзакция 3: 11 января 2022 года, 100 USD
        Transaction transaction3 = new Transaction();
        transaction3.setExpenseCategory(category);
        transaction3.setSumUSD(BigDecimal.valueOf(100));
        transaction3.setLimit(limit2);
        transaction3.setDateTime(ZonedDateTime.parse("2022-01-11T00:00:00Z"));

        // Транзакция 4: 12 января 2022 года, 700 USD
        Transaction transaction4 = new Transaction();
        transaction4.setExpenseCategory(category);
        transaction4.setSumUSD(BigDecimal.valueOf(700));
        transaction4.setLimit(limit2);
        transaction4.setDateTime(ZonedDateTime.parse("2022-01-12T00:00:00Z"));

        // Транзакция 5: 13 января 2022 года, 100 USD
        Transaction transaction5 = new Transaction();
        transaction5.setExpenseCategory(category);
        transaction5.setSumUSD(BigDecimal.valueOf(100));
        transaction5.setLimit(limit2);
        transaction5.setDateTime(ZonedDateTime.parse("2022-01-13T00:00:00Z"));

        // Транзакция 6: 13 января 2022 года, 100 USD
        Transaction transaction6 = new Transaction();
        transaction6.setExpenseCategory(category);
        transaction6.setSumUSD(BigDecimal.valueOf(100));
        transaction6.setLimit(limit2);
        transaction6.setDateTime(ZonedDateTime.parse("2022-01-13T00:00:00Z"));

        // Транзакция 1: Лимит не превышен
        assertFalse(transactionService.isLimitExceeded(transaction1));

        // Транзакция 2: Лимит превышен
        assertTrue(transactionService.isLimitExceeded(transaction2));

        // Транзакция 3: Лимит не превышен
        assertFalse(transactionService.isLimitExceeded(transaction3));

        // Транзакция 4: Лимит не превышен
        assertFalse(transactionService.isLimitExceeded(transaction4));

        // Транзакция 5: Лимит не превышен
        assertFalse(transactionService.isLimitExceeded(transaction5));

        // Транзакция 6: Лимит превышен
        assertTrue(transactionService.isLimitExceeded(transaction6));
    }
}
