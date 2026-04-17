package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionSplitUseCase;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBFindCurrencyPort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionFindByIdPort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.List;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;
import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.*;

public class SplitConsumptionSplitService implements ConsumptionSplitUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final ConsumptionDBSavePort consumptionDBSavePort;
    private final ConsumptionFindByIdPort consumptionFindByIdPort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationConsumption mapperApplicationConsumption;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final GenericEventPublisherPort genericEventPublisherPort;

    public SplitConsumptionSplitService(CardFindByIdPort cardFindByIdPort,
                                        BalanceDBFindByIdPort balanceDBFindByIdPort,
                                        BalanceDBSavePort balanceDBSavePort,
                                        ConsumptionDBSavePort consumptionDBSavePort,
                                        ConsumptionFindByIdPort consumptionFindByIdPort,
                                        LoadCurrencyPort loadCurrencyPort,
                                        CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                        ConsumptionDBFindCurrencyPort consumptionDBFindCurrencyPort,
                                        MapperApplicationCurrency mapperApplicationCurrency,
                                        MapperApplicationConsumption mapperApplicationConsumption,
                                        MapperApplicationCard mapperApplicationCard,
                                        MapperApplicationBalance mapperApplicationBalance, GenericEventPublisherPort genericEventPublisherPort) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.consumptionDBSavePort = consumptionDBSavePort;
        this.consumptionFindByIdPort = consumptionFindByIdPort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.consumptionDBFindCurrencyPort = consumptionDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationConsumption = mapperApplicationConsumption;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.genericEventPublisherPort = genericEventPublisherPort;
    }

    @Override
    public List<ConsumptionId> execute(CardSplitConsumptionCommand cardSplitConsumptionCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardSplitConsumptionCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var consumptionCurrencyValue = consumptionDBFindCurrencyPort
                .load(cardSplitConsumptionCommand.consumptionId(), cardSplitConsumptionCommand.cardId().toString())
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var consumptionCurrencyDto = loadCurrencyPort.load(consumptionCurrencyValue)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);
        var consumptionCurrency = mapperApplicationCurrency.toDtoRequest(consumptionCurrencyDto);

        var consumptionResponseDto = consumptionFindByIdPort
                .load(cardSplitConsumptionCommand.consumptionId(), cardSplitConsumptionCommand.cardId().toString(), consumptionCurrency)
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_NOT_FOUND));

        var cardResponseDto = cardFindByIdPort
                .load(cardSplitConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardSplitConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var consumption = mapperApplicationConsumption.toDomain(consumptionResponseDto);
        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);


        var consumptions = consumption.splitConsumption(cardSplitConsumptionCommand.installments(), card.getCredit().getDebtTax());
        balance.cancelConsumption(consumption.getConsumptionAmount());
        consumption.close();
        card.updateStatus(balance.isOvercharged());

        consumptions.stream()
                .map(Consumption::getConsumptionAmount)
                .forEach(amount -> {
                    balance.consumeBalance(amount, card.getCardStatus());
                    card.updateStatus(balance.isOvercharged());
                });

        var consumptionIds = consumptions.stream()
                .map(mapperApplicationConsumption::toDto)
                .map(consumptionDBSavePort::save)
                .map(opt -> opt
                        .orElseThrow(() -> new ApplicationConsumptionException(FAILED_TO_UPDATE_CONSUMPTION)))
                .toList();

        var consumptionRequestDto = mapperApplicationConsumption.toDto(consumption);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        this.consumptionDBSavePort.save(consumptionRequestDto).orElseThrow(() -> new ApplicationConsumptionException(FAILED_TO_UPDATE_CONSUMPTION));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));

        card.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        balance.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        consumption.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        consumptions.forEach(consum -> consum.pullDomainEvents().forEach(genericEventPublisherPort::publish));

        return consumptionIds;
    }
}
