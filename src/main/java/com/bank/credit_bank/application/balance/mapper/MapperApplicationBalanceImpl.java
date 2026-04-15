package com.bank.credit_bank.application.balance.mapper;

import com.bank.credit_bank.application.balance.dto.request.BalanceRequestDto;
import com.bank.credit_bank.application.balance.dto.response.BalanceResponseDto;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.vo.CardId;

public class MapperApplicationBalanceImpl implements MapperApplicationBalance {

    @Override
    public Balance toDomain(BalanceResponseDto dto) {
        CurrencyEnum currencyEnum = CurrencyEnum.valueOf(dto.currency());

        Currency currency = Currency.create(currencyEnum, dto.exchangeRate());
        Amount total    = Amount.create(currency, dto.total());
        Amount old      = Amount.create(currency, dto.old());
        Amount available = Amount.create(currency, dto.available());
        DateRange dateRange = DateRange.create(dto.startDate(), dto.endDate());
        CardId cardId = CardId.create(dto.cardId());
        BalanceId balanceId = BalanceId.create(dto.id());
        StatusEnum status = StatusEnum.ofValue(dto.status()).orElse(StatusEnum.ACTIVE);

        return new Balance(
                balanceId,
                status,
                dto.createdDate(),
                dto.updatedDate(),
                cardId,
                total,
                old,
                dateRange,
                available
        );
    }

    @Override
    public BalanceRequestDto toDto(Balance balance) {
        return new BalanceRequestDto(
                balance.getId().getValue(),
                balance.getStatus().getValue(),
                balance.getCreatedDate(),
                balance.getUpdatedDate(),
                balance.getCardId().getValue(),
                balance.getTotal().getCurrency().getCurrency().getValue(),
                balance.getTotal().getCurrency().getExchangeRate(),
                balance.getTotal().getAmount(),
                balance.getOld().getAmount(),
                balance.getAvailable().getAmount(),
                balance.getDateRange().getStartDate(),
                balance.getDateRange().getEndDate()
        );
    }
}

