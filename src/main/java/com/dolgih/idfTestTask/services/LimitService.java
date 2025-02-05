package com.dolgih.idfTestTask.services;

import com.dolgih.idfTestTask.dtos.requests.LimitRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.LimitResponseDTO;
import com.dolgih.idfTestTask.entities.Limit;
import com.dolgih.idfTestTask.enums.ExpenseCategory;
import com.dolgih.idfTestTask.mappers.LimitMapper;
import com.dolgih.idfTestTask.repositories.LimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimitService {
    private final LimitRepository limitRepository;
    private final LimitMapper limitMapper;

    public Optional<Limit> getCurrentLimit(ExpenseCategory expenseCategory) {

        return limitRepository.findCurrentLimit(expenseCategory);
    }

    public LimitResponseDTO createNewLimit(LimitRequestDTO limitRequestDTO) {
        Limit limit = limitMapper.limitRequestDTOtoLimit(limitRequestDTO);

        limitRepository.save(limit);
        log.info("Создаем новый лимит для категории {} - {} USD",
                 limitRequestDTO.getExpenseCategory(), limitRequestDTO.getAmountUSD());

        return limitMapper.limitToLimitResponseDTO(limit);
    }

    public List<LimitResponseDTO> getAllLimits() {
        log.info("Поступил запрос на получение всех лимитов...");

        return limitRepository.findAll()
                              .stream()
                              .map(limitMapper::limitToLimitResponseDTO)
                              .collect(Collectors.toList());
    }
}
