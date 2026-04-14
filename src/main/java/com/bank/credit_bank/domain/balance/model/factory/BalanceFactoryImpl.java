package com.bank.credit_bank.domain.balance.model.factory;

import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.exceptions.BalanceException;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.base.vo.DateRange;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.generic.exceptions.EntityException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.bank.credit_bank.domain.balance.model.constants.BalanceErrorMessage.*;
import static com.bank.credit_bank.domain.base.enums.StatusEnum.ACTIVE;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class BalanceFactoryImpl implements BalanceFactory {

    @Override
    public Balance create(Long balanceId,
                          Long cardId,
                          BigDecimal total,
                          LocalDate startDate,
                          LocalDate endDate,
                          Integer currency,
                          BigDecimal exchangeRate,
                          Integer status,
                          LocalDateTime createdDate,
                          LocalDateTime updatedDate) {
        try {
            isNotNull(balanceId, new BalanceException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BalanceException(CARD_ID_CANNOT_BE_NULL));
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            isNotNull(startDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(endDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(currency, new BalanceException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new BalanceException(EXCHANGE_RATE_CANNOT_BE_NULL));

            var curr = Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(), exchangeRate);
            var totalAmount = Amount.create(curr, total);

            return new Balance(
                    BalanceId.create(balanceId),
                    StatusEnum.ofValue(status).orElseThrow(),
                    createdDate,
                    updatedDate,
                    CardId.create(cardId),
                    totalAmount,
                    totalAmount,
                    DateRange.create(startDate, endDate),
                    totalAmount
            );
        } catch (EntityException e) {
            throw new BalanceException(e.getMessage());
        }
    }

    @Override
    public Balance create(Long balanceId,
                          Long cardId,
                          BigDecimal total,
                          BigDecimal old,
                          BigDecimal available,
                          LocalDate startDate,
                          LocalDate endDate,
                          Integer currency,
                          BigDecimal exchangeRate) {
        try {
            isNotNull(balanceId, new BalanceException(ID_CANNOT_BE_NULL));
            isNotNull(cardId, new BalanceException(CARD_ID_CANNOT_BE_NULL));
            isNotNull(total, new BalanceException(TOTAL_AMOUNT_CANNOT_BE_NULL));
            isNotNull(old, new BalanceException(OLD_AMOUNT_CANNOT_BE_NULL));
            isNotNull(available, new BalanceException(AVAILABLE_AMOUNT_CANNOT_BE_NULL));
            isNotNull(startDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(endDate, new BalanceException(DATE_RANGE_CANNOT_BE_NULL));
            isNotNull(currency, new BalanceException(CURRENCY_CANNOT_BE_NULL));
            isNotNull(exchangeRate, new BalanceException(EXCHANGE_RATE_CANNOT_BE_NULL));

            Currency curr = Currency.create(CurrencyEnum.ofValue(currency).orElseThrow(), exchangeRate);

            return new Balance(
                    BalanceId.create(balanceId),
                    ACTIVE,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    CardId.create(cardId),
                    Amount.create(curr, total),
                    Amount.create(curr, old),
                    DateRange.create(startDate, endDate),
                    Amount.create(curr, available)
            );
        } catch (EntityException e) {
            throw new BalanceException(e.getMessage());
        }
    }
}
