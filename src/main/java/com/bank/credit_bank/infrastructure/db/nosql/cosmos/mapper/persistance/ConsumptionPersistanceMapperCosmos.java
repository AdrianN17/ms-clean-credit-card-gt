package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.ConsumptionEntityCosmos;

public interface ConsumptionPersistanceMapperCosmos {
    ConsumptionResponseDto toDomain(ConsumptionEntityCosmos consumptionEntity, CurrencyRequestDto currency);

    ConsumptionEntityCosmos toEntity(ConsumptionRequestDto consumption);
}
