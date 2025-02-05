package com.dolgih.idfTestTask.mappers;

import com.dolgih.idfTestTask.dtos.requests.LimitRequestDTO;
import com.dolgih.idfTestTask.dtos.responses.LimitResponseDTO;
import com.dolgih.idfTestTask.entities.Limit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitMapper {
    Limit limitRequestDTOtoLimit(LimitRequestDTO limitRequestDTO);
    LimitResponseDTO limitToLimitResponseDTO(Limit limit);
}
