package com.bank.credit_bank.application.consumption.mapper;

import com.bank.credit_bank.application.consumption.dto.request.ConsumptionRequestDto;
import com.bank.credit_bank.application.consumption.dto.response.ConsumptionResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;

public class MapperApplicationConsumptionImpl implements MapperApplicationConsumption {

    @Override
    public Consumption toDomain(ConsumptionResponseDto dto) {
        CurrencyEnum currencyEnum = CurrencyEnum.valueOf(dto.currency());
        Currency currency = Currency.create(currencyEnum, dto.exchangeRate());
        Amount amount = Amount.create(currency, dto.amount());
        CardId cardId = CardId.create(dto.cardId());
        StatusEnum status = StatusEnum.ofValue(dto.status()).orElse(StatusEnum.ACTIVE);

        return Consumption.builder()
                .id(dto.id())
                .status(status)
                .createdDate(dto.createdDate())
                .updatedDate(dto.updatedDate())
                .consumptionAmount(amount)
                .consumptionApprobation(dto.consumptionDate(), dto.consumptionApprobationDate())
                .cardId(cardId)
                .sellerName(dto.sellerName())
                .build();
    }

    @Override
    public ConsumptionRequestDto toDto(Consumption consumption) {
        return new ConsumptionRequestDto(
                consumption.getId().getValue(),
                consumption.getStatus().getValue(),
                consumption.getCreatedDate(),
                consumption.getUpdatedDate(),
                consumption.getConsumptionAmount().getCurrency().getCurrency().getValue(),
                consumption.getConsumptionAmount().getCurrency().getExchangeRate(),
                consumption.getConsumptionAmount().getAmount(),
                consumption.getConsumptionApprobation().getDate(),
                consumption.getConsumptionApprobation().getApprobationDate(),
                consumption.getCardId().getValue(),
                consumption.getSellerName().getValue()
        );
    }
}

