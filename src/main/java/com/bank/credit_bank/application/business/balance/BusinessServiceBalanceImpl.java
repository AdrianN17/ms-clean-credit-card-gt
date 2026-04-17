package com.bank.credit_bank.application.business.balance;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.balance.model.entities.Balance;
import com.bank.credit_bank.domain.balance.model.vo.BalanceId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_CREATE_BALANCE;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;

public class BusinessServiceBalanceImpl implements BusinessServiceBalance {

    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final LoadCurrencyPort loadCurrencyPort;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final BalanceDBSavePort balanceDBSavePort;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public BusinessServiceBalanceImpl(BalanceDBFindByIdPort balanceDBFindByIdPort,
                                      CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                      MapperApplicationCurrency mapperApplicationCurrency,
                                      LoadCurrencyPort loadCurrencyPort,
                                      MapperApplicationBalance mapperApplicationBalance, BalanceDBSavePort balanceDBSavePort, GenericEventPublisherPort genericEventPublisherPort) {
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.loadCurrencyPort = loadCurrencyPort;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.balanceDBSavePort = balanceDBSavePort;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public Balance get(Long cardId) {
        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardId)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardId, cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        return mapperApplicationBalance.toDomain(balanceResponseDto);
    }

    @Override
    public BalanceId save(Balance balance) {

        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        var id = balanceDBSavePort.save(balanceRequestDto)
                .orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_CREATE_BALANCE));

        balance.pullDomainEvents().forEach(genericEventPublisherPort::publish);

        return id;
    }
}
