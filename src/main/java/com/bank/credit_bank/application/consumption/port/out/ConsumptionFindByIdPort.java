package com.bank.credit_bank.application.consumption.port.out;

import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;

import java.util.Optional;
import java.util.UUID;

@FunctionalInterface
public interface ConsumptionFindByIdPort {
    Optional<ConsumptionResponseDto> load(UUID consumptonId, String cardId, CurrencyRequestDto currency);
}
