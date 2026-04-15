package com.bank.credit_bank.application.card.mapper;

import com.bank.credit_bank.application.card.dto.request.CardRequestDto;
import com.bank.credit_bank.application.card.dto.response.CardResponseDto;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.domain.card.model.vo.CardAccountId;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.card.model.vo.Credit;
import com.bank.credit_bank.domain.card.model.vo.PaymentDay;

public class MapperApplicationCardImpl implements MapperApplicationCard {

    @Override
    public Card toDomain(CardResponseDto dto) {
        CardId cardId = CardId.create(dto.id());
        StatusEnum status = StatusEnum.ofValue(dto.status()).orElse(StatusEnum.ACTIVE);

        TypeCardEnum typeCard = TypeCardEnum.valueOf(dto.typeCard());
        CategoryCardEnum categoryCard = CategoryCardEnum.valueOf(dto.categoryCard());
        CardStatusEnum cardStatus = CardStatusEnum.valueOf(dto.cardStatus());

        CurrencyEnum currencyEnum = CurrencyEnum.valueOf(dto.currency());
        Currency currency = Currency.create(currencyEnum, dto.exchangeRate());
        Amount creditTotal = Amount.create(currency, dto.creditTotal());
        Credit credit = Credit.create(creditTotal, dto.debtTax());

        CardAccountId cardAccountId = CardAccountId.create(dto.cardAccountId());
        PaymentDay paymentDay = PaymentDay.create(dto.paymentDay());

        return new Card(
                cardId,
                status,
                dto.createdDate(),
                dto.updatedDate(),
                typeCard,
                categoryCard,
                credit,
                cardStatus,
                cardAccountId,
                paymentDay
        );
    }

    @Override
    public CardRequestDto toDto(Card card) {
        return new CardRequestDto(
                card.getId().getValue(),
                card.getStatus().getValue(),
                card.getCreatedDate(),
                card.getUpdatedDate(),
                card.getTypeCard().getValue(),
                card.getCategoryCard().getValue(),
                card.getCredit().getCreditTotal().getCurrency().getCurrency().getValue(),
                card.getCredit().getCreditTotal().getCurrency().getExchangeRate(),
                card.getCredit().getCreditTotal().getAmount(),
                card.getCredit().getDebtTax(),
                card.getCardStatus().getValue(),
                card.getCardAccountId().getValue(),
                card.getPaymentDay().getValue()
        );
    }
}

