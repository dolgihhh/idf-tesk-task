package com.dolgih.idfTestTask.repositories;

import com.dolgih.idfTestTask.entities.Limit;
import com.dolgih.idfTestTask.enums.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Integer> {

    @Query("SELECT l FROM Limits l WHERE l.expenseCategory = :expenseCategory ORDER BY l.dateSet " +
           "DESC LIMIT 1")
    Optional<Limit> findCurrentLimit(@Param("expenseCategory") ExpenseCategory expenseCategory);
}
