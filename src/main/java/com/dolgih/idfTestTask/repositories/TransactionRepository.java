package com.dolgih.idfTestTask.repositories;

import com.dolgih.idfTestTask.entities.Transaction;
import com.dolgih.idfTestTask.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT COALESCE(SUM(t.sumUSD), 0) FROM Transactions t WHERE YEAR(t.dateTime) = :year " +
           "AND MONTH(t.dateTime) = :month AND t.expenseCategory = :expenseCategory")
    BigDecimal getTotalTransactionsSumPerMonth(@Param("year") int year, @Param("month") int month,
                                              @Param("expenseCategory") ExpenseCategory expenseCategory);

    @Query("SELECT t FROM Transactions t WHERE t.limitExceeded = true")
    List<Transaction> getAllLimitExceededTransactions();
}
