package com.bank.credit_bank.application.consumption.mapper;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;

public interface MapperApplicationConsumption {
    Consumption toDomain(ConsumptionResponseDto dto);
    ConsumptionRequestDto toDto(Consumption consumption);
}
