package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.application.currency.dto.request.CurrencyRequestDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.ConsumptionEntityMongo;

public class ConsumptionPersistanceMapperMongoImpl implements ConsumptionPersistanceMapperMongo {

    @Override
    public ConsumptionResponseDto toDomain(ConsumptionEntityMongo entity, CurrencyRequestDto currency) {
        return new ConsumptionResponseDto(
                entity.getConsumptionId(),
                entity.getStatus().getValue(),
                entity.getCreatedDate(),
                entity.getUpdatedDate(),
                entity.getCurrency().name(),
                currency.exchangeRate(),
                entity.getAmount(),
                entity.getConsumptionDate(),
                entity.getConsumptionApprobationDate(),
                Long.parseLong(entity.getCardId()),
                entity.getSellerName()
        );
    }

    @Override
    public ConsumptionEntityMongo toEntity(ConsumptionRequestDto consumption) {
        return ConsumptionEntityMongo.builder()
                .consumptionId(consumption.id())
                .amount(consumption.amount())
                .currency(CurrencyEnum.ofValue(consumption.currency()).orElseThrow())
                .consumptionDate(consumption.consumptionDate())
                .consumptionApprobationDate(consumption.consumptionApprobationDate())
                .sellerName(consumption.sellerName())
                .cardId(consumption.cardId().toString())
                .createdDate(consumption.createdDate())
                .updatedDate(consumption.updatedDate())
                .status(StatusEnum.ofValue(consumption.status()).orElseThrow())
                .build();
    }
}
