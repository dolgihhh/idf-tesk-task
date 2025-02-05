package com.dolgih.idfTestTask.mappers;

import java.math.BigDecimal;
import com.dolgih.idfTestTask.dtos.requests.TransactionRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionLimitResponseDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionResponseDTO;
import com.dolgih.idfTestTask.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {
    @Value("${default.monthly.limit}")
    protected BigDecimal defaultMonthlyLimit;

    public abstract Transaction transactionRequestDTOtoTransaction(TransactionRequestDTO transactionRequestDTO);
    public abstract TransactionResponseDTO transactionToTransactionResponseDTO(Transaction transaction);

    @Mapping(target = "amountUSD", source = "limit.amountUSD", defaultExpression = "java(this.defaultMonthlyLimit)")
    @Mapping(target = "dateSet", source = "limit.dateSet")
    public abstract TransactionLimitResponseDTO transactionToTransactionLimitResponseDTO(Transaction transaction);
}
