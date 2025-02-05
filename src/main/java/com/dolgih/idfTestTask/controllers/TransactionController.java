package com.dolgih.idfTestTask.controllers;

import com.dolgih.idfTestTask.dtos.requests.TransactionRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionLimitResponseDTO;
import com.dolgih.idfTestTask.dtos.responses.TransactionResponseDTO;
import com.dolgih.idfTestTask.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@Tag(name = "Транзакции", description = "Операции с транзакциями")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Создание транзакции", description = "Позволяет создать новую " +
                                                              "транзакцию", responses = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully created",
                         content = @Content(
                                 schema = @Schema(implementation = TransactionResponseDTO.class)))})
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Valid @RequestBody
                                                                    TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO transactionResponseDTO =
                transactionService.createTransaction(transactionRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(transactionResponseDTO);
    }

    @GetMapping("/limit-exceeded")
    @Operation(summary = "Получение транзакций, превысивших лимит", description = "Позволяет " +
                                                                                  "получить все " +
                                                                                  "транзакции, " +
                                                                                  "превысившие " +
                                                                                  "месячный лимит")
    public ResponseEntity<List<TransactionLimitResponseDTO>> getLimitExceededTransactions() {
        List<TransactionLimitResponseDTO> transactions =
                transactionService.getAllLimitExceededTransactions();

        return ResponseEntity.ok(transactions);
    }
}
