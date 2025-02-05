package com.dolgih.idfTestTask.repositories;

import com.dolgih.idfTestTask.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {

    @Query("SELECT e FROM ExchangeRates e WHERE e.currencyPair = :currencyPair ORDER BY e.date " +
           "DESC, e.id DESC LIMIT 1")
    Optional<ExchangeRate> findCurrentExchangeRate(@Param("currencyPair") String currencyPair);
}
