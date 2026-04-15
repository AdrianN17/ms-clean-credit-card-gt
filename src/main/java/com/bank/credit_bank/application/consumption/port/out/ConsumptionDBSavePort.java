package com.bank.credit_bank.application.consumption.port.out;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.Optional;

@FunctionalInterface
public interface ConsumptionDBSavePort {
    Optional<ConsumptionId> save(ConsumptionRequestDto consumption);
}
