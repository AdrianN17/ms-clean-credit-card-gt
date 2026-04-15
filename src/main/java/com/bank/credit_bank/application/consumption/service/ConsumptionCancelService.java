package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionCancelUseCase;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;
import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.*;

public class ConsumptionCancelService implements ConsumptionCancelUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final ConsumptionFindByIdPort consumptionFindByIdPort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final ConsumptionDBSavePort consumptionDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationConsumption mapperApplicationConsumption;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;

    public ConsumptionCancelService(CardFindByIdPort cardFindByIdPort,
                                    BalanceDBFindByIdPort balanceDBFindByIdPort,
                                    ConsumptionFindByIdPort consumptionFindByIdPort,
                                    BalanceDBSavePort balanceDBSavePort,
                                    ConsumptionDBSavePort consumptionDBSavePort,
                                    LoadCurrencyPort loadCurrencyPort,
                                    CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                    ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort,
                                    MapperApplicationCurrency mapperApplicationCurrency,
                                    MapperApplicationConsumption mapperApplicationConsumption,
                                    MapperApplicationCard mapperApplicationCard,
                                    MapperApplicationBalance mapperApplicationBalance) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.consumptionFindByIdPort = consumptionFindByIdPort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.consumptionDBSavePort = consumptionDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.consumptionDBFindCurrencyPort = consumptionDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationConsumption = mapperApplicationConsumption;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
    }

    @Override
    public ConsumptionId execute(CardCancelConsumptionCommand cardCancelConsumptionCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardCancelConsumptionCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var consumptionCurrencyValue = consumptionDBFindCurrencyPort
                .load(cardCancelConsumptionCommand.consumptionId(), cardCancelConsumptionCommand.cardId().toString())
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var consumptionCurrencyDto = loadCurrencyPort.load(consumptionCurrencyValue)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);
        var consumptionCurrency = mapperApplicationCurrency.toDtoRequest(consumptionCurrencyDto);

        var consumptionResponseDto = consumptionFindByIdPort
                .load(cardCancelConsumptionCommand.consumptionId(), cardCancelConsumptionCommand.cardId().toString(), consumptionCurrency)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        var cardResponseDto = cardFindByIdPort
                .load(cardCancelConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardCancelConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var consumption = mapperApplicationConsumption.toDomain(consumptionResponseDto);
        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);

        card.cancelConsumption(balance, consumption);

        var consumptionRequestDto = mapperApplicationConsumption.toDto(consumption);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        this.consumptionDBSavePort.save(consumptionRequestDto).orElseThrow(() -> new ApplicationConsumptionException(FAILED_TO_UPDATE_CONSUMPTION));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));

        return consumption.getId();
    }
}
