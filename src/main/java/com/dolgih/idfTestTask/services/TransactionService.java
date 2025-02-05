package com.dolgih.idfTestTask.services;

import com.dolgih.idfTestTask.dtos.requests.TransactionRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionLimitResponseDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionResponseDTO;
import com.dolgih.idfTestTask.entities.ExchangeRate;
import com.dolgih.idfTestTask.entities.Limit;
import com.dolgih.idfTestTask.entities.Transaction;
import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.dolgih.idfTestTask.mappers.TransactionMapper;
import com.dolgih.idfTestTask.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionMapper transactionMapper;
    private final ExchangeRateService exchangeRateService;
    private final LimitService limitService;
    private final TransactionRepository transactionRepository;

    @Value("${default.monthly.limit}")
    private BigDecimal defaultMonthlyLimit;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        log.info("Создаем новую транзакцию...");
        Transaction newTransaction = transactionMapper.transactionRequestDTOtoTransaction(
                transactionRequestDTO);

        log.info("Получаем курс валют...");
        Optional<ExchangeRate> opCurrentExchangeRate = exchangeRateService.getLatestExchangeRate(
                transactionRequestDTO.getCurrencyShortName());

        BigDecimal newTransactionSumUSD;
        ExchangeRate currentExchangeRate;
        if (opCurrentExchangeRate.isPresent()) {
            currentExchangeRate = opCurrentExchangeRate.get();
            newTransactionSumUSD = newTransaction.getSum().divide(currentExchangeRate.getRate(), 2,
                                                                  RoundingMode.HALF_EVEN);
            log.info("Сумма транзакции в USD - {}$", newTransactionSumUSD);
        } else {
            currentExchangeRate = null;
            newTransactionSumUSD = BigDecimal.ZERO;
            log.info("Сумма транзакции в USD не посчитана, нет курса");
        } //на случай если в БД нет курсов и внешнее api не работает

        Optional<Limit> opCurrentLimit = limitService.getCurrentLimit(transactionRequestDTO.getExpenseCategory());
        Limit currentLimit = opCurrentLimit.orElse(null);
        log.info("Установленный в USD лимит на месяц по категории {} - {}$",
                 newTransaction.getExpenseCategory(), Optional.ofNullable(currentLimit)
                                                              .map(Limit::getAmountUSD)
                                                              .orElse(defaultMonthlyLimit));

        newTransaction.setExchangeRate(currentExchangeRate);
        newTransaction.setLimit(currentLimit);
        newTransaction.setSumUSD(newTransactionSumUSD);
        newTransaction.setLimitExceeded(isLimitExceeded(newTransaction));

        transactionRepository.save(newTransaction);

        return transactionMapper.transactionToTransactionResponseDTO(newTransaction);
    }

    public BigDecimal calculateMonthlySumByCategory(ZonedDateTime dateTime,
                                                    ExpenseCategory expenseCategory) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();

        return transactionRepository.getTotalTransactionsSumPerMonth(year, month, expenseCategory);
    }

    public Boolean isLimitExceeded(Transaction newTransaction) {
        BigDecimal limitUSD;
        if (newTransaction.getLimit() == null) {
            limitUSD = defaultMonthlyLimit;
        } else {
            limitUSD = newTransaction.getLimit().getAmountUSD();
        }

        BigDecimal beforeTransactionMonthlySpentPerMonthUSD =
                calculateMonthlySumByCategory(newTransaction.getDateTime(),
                                              newTransaction.getExpenseCategory());

        BigDecimal monthlySumSpentPerMonth = newTransaction.getSumUSD()
                                                           .add(beforeTransactionMonthlySpentPerMonthUSD);

        log.info("Сумма трат за месяц в USD по категории {} c учетом текущей транзакции: {}$",
                 newTransaction.getExpenseCategory(), monthlySumSpentPerMonth);

        return monthlySumSpentPerMonth.compareTo(limitUSD) > 0;
    }

    public List<TransactionLimitResponseDTO> getAllLimitExceededTransactions() {
        log.info("Получаем все транзакции, превысившие лимит...");

        return transactionRepository.getAllLimitExceededTransactions()
                                    .stream()
                                    .map(transactionMapper::transactionToTransactionLimitResponseDTO)
                                    .collect(Collectors.toList());
    }
}
