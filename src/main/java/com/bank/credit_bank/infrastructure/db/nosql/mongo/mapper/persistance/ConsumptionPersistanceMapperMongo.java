package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.ConsumptionEntityMongo;

public interface ConsumptionPersistanceMapperMongo {
    ConsumptionResponseDto toDomain(ConsumptionEntityMongo consumptionEntity, CurrencyRequestDto currency);

    ConsumptionEntityMongo toEntity(ConsumptionRequestDto consumption);
}
