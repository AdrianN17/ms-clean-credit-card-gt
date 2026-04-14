package com.bank.credit_bank.domain.card.model.factory;

import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.enums.CardStatusEnum;
import com.bank.credit_bank.domain.card.model.enums.CategoryCardEnum;
import com.bank.credit_bank.domain.card.model.enums.TypeCardEnum;
import com.bank.credit_bank.domain.card.model.exceptions.CardException;
import com.bank.credit_bank.domain.card.model.vo.CardAccountId;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.card.model.vo.Credit;
import com.bank.credit_bank.domain.card.model.vo.PaymentDay;
import com.bank.credit_bank.domain.generic.exceptions.EntityException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.card.model.constants.CardAccountIdErrorMessage.CARD_ACCOUNTID_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.card.model.constants.CardErrorMessage.*;
import static com.bank.credit_bank.domain.card.model.enums.CardStatusEnum.OPERATIVE;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class CardFactoryImpl implements CardFactory {

    @Override
    public Card create(Long cardId,
                       Integer typeCard,
                       Integer categoryCard,
                       Long cardAccountId,
                       Short paymentDay,
                       BigDecimal creditTotal,
                       BigDecimal debtTax,
                       Integer currency,
                       BigDecimal exchangeRate,
                       Integer cardStatus,
                       Integer status,
                       LocalDateTime createdDate,
                       LocalDateTime updatedDate) {
        try {

            isNotNull(typeCard, new CardException(TYPE_CARD_CANNOT_BE_NULL));
            isNotNull(categoryCard, new CardException(CATEGORY_CARD_CANNOT_BE_NULL));
            isNotNull(creditTotal, new CardException(CREDIT_TOTAL_CANNOT_BE_NULL));
            isNotNull(debtTax, new CardException(DEBT_TAX_CANNOT_BE_NULL));
            isNotNull(currency, new CardException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new CardException(EXCHANGE_RATE_CANNOT_BE_NULL));
            isNotNull(cardStatus, new CardException(CARD_STATUS_CANNOT_BE_NULL));
            isNotNull(cardAccountId, new CardException(CARD_ACCOUNTID_CANNOT_BE_NULL));
            isNotNull(paymentDay, new CardException(PAYMENT_DAY_CANNOT_BE_NULL));

            Credit credit = Credit.create(
                    Amount.create(
                            Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(),
                                    exchangeRate),
                            creditTotal)
                    , debtTax);

            return new Card(
                    CardId.create(cardId),
                    StatusEnum.ofValue(status).orElseThrow(),
                    createdDate,
                    updatedDate,
                    TypeCardEnum.ofValue(typeCard).orElseThrow(),
                    CategoryCardEnum.ofValue(categoryCard).orElseThrow(),
                    credit,
                    CardStatusEnum.ofValue(cardStatus).orElseThrow(),
                    CardAccountId.create(cardAccountId),
                    PaymentDay.create(paymentDay)
            );
        } catch (EntityException e) {
            throw new CardException(e.getMessage());
        }
    }

    @Override
    public Card create(Long cardId,
                       Integer typeCard,
                       Integer categoryCard,
                       Long cardAccountId,
                       Short paymentDay,
                       BigDecimal creditTotal,
                       BigDecimal debtTax,
                       Integer currency,
                       BigDecimal exchangeRate) {
        try {

            isNotNull(cardId, new CardException(ID_CANNOT_BE_NULL));
            isNotNull(cardAccountId, new CardException(CARD_ACCOUNTID_CANNOT_BE_NULL));
            isNotNull(typeCard, new CardException(TYPE_CARD_CANNOT_BE_NULL));
            isNotNull(categoryCard, new CardException(CATEGORY_CARD_CANNOT_BE_NULL));
            isNotNull(creditTotal, new CardException(CREDIT_TOTAL_CANNOT_BE_NULL));
            isNotNull(debtTax, new CardException(DEBT_TAX_CANNOT_BE_NULL));
            isNotNull(currency, new CardException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new CardException(EXCHANGE_RATE_CANNOT_BE_NULL));
            isNotNull(paymentDay, new CardException(PAYMENT_DAY_CANNOT_BE_NULL));

            Credit credit = Credit.create(
                    Amount.create(
                            Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(),
                                    exchangeRate),
                            creditTotal)
                    , debtTax);

            return new Card(
                    CardId.create(cardId),
                    ACTIVE,
                    null,
                    null,
                    TypeCardEnum.ofValue(typeCard).orElseThrow(),
                    CategoryCardEnum.ofValue(categoryCard).orElseThrow(),
                    credit,
                    OPERATIVE,
                    CardAccountId.create(cardAccountId),
                    PaymentDay.create(paymentDay)
            );
        } catch (EntityException e) {
            throw new CardException(e.getMessage());
        }
    }
}
