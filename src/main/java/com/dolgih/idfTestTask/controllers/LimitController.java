package com.dolgih.idfTestTask.controllers;

import com.dolgih.idfTestTask.dtos.requests.LimitRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.LimitResponseDTO;
import com.dolgih.idfTestTask.services.LimitService;
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
@RequestMapping("/api/limits")
@Tag(name = "Лимиты", description = "Взаимодействие с лимитами")
public class LimitController {
    private final LimitService limitService;

    @PostMapping
    @Operation(summary = "Создание нового лимита", description = "Позволяет создать новый лимит " +
                                                                 "для указанной категории трат",
               responses = {
                       @ApiResponse(responseCode = "201",
                                    description = "Limit successfully created",
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = LimitResponseDTO.class)))})
    public ResponseEntity<LimitResponseDTO> createLimit(@Valid @RequestBody LimitRequestDTO limitRequestDTO) {
        LimitResponseDTO limitResponseDTO = limitService.createNewLimit(limitRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(limitResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Получение лимитов", description = "Позволяет получить все лимиты")
    public ResponseEntity<List<LimitResponseDTO>> getLimits(){
        List<LimitResponseDTO> limits = limitService.getAllLimits();

        return ResponseEntity.ok(limits);
    }
}
