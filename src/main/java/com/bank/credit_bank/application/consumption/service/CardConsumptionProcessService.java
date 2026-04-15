package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.consumption.mapper.MapperApplicationConsumption;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionProcessUseCase;
import com.bank.credit_bank.application.consumption.port.out.ConsumptionDBSavePort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.vo.CardId;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.BENEFIT_NOT_FOUND;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_UPDATE_BENEFIT;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_NOT_FOUND;
import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.CONSUMPTION_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.FAILED_TO_CREATE_CONSUMPTION;

public class CardConsumptionProcessService implements ConsumptionProcessUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final BenefitDBFindByIdPort benefitDBFindByIdPort;
    private final BenefitDBSavePort benefitDBSavePort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final ConsumptionDBSavePort consumptionDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationConsumption mapperApplicationConsumption;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final MapperApplicationBenefit mapperApplicationBenefit;

    public CardConsumptionProcessService(CardFindByIdPort cardFindByIdPort,
                                         BalanceDBFindByIdPort balanceDBFindByIdPort,
                                         BenefitDBFindByIdPort benefitDBFindByIdPort,
                                         BenefitDBSavePort benefitDBSavePort,
                                         BalanceDBSavePort balanceDBSavePort,
                                         ConsumptionDBSavePort consumptionDBSavePort,
                                         LoadCurrencyPort loadCurrencyPort,
                                         CardDBFindCurrencyPort cardDBFindCurrencyPort,
                                         MapperApplicationCurrency mapperApplicationCurrency,
                                         MapperApplicationConsumption mapperApplicationConsumption,
                                         MapperApplicationCard mapperApplicationCard,
                                         MapperApplicationBalance mapperApplicationBalance,
                                         MapperApplicationBenefit mapperApplicationBenefit) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.benefitDBFindByIdPort = benefitDBFindByIdPort;
        this.benefitDBSavePort = benefitDBSavePort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.consumptionDBSavePort = consumptionDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationConsumption = mapperApplicationConsumption;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.mapperApplicationBenefit = mapperApplicationBenefit;
    }

    @Override
    public ConsumptionId execute(CardProcessConsumptionCommand cardProcessConsumptionCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardProcessConsumptionCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var consumptionCurrencyDto = loadCurrencyPort.load(cardProcessConsumptionCommand.currency())
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);
        var consumptionCurrency = mapperApplicationCurrency.toDtoRequest(consumptionCurrencyDto);

        var cardResponseDto = cardFindByIdPort
                .load(cardProcessConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardProcessConsumptionCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var benefitResponseDto = benefitDBFindByIdPort
                .load(cardProcessConsumptionCommand.cardId())
                .orElseThrow(() -> new ApplicationBenefitException(BENEFIT_NOT_FOUND));

        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);
        var benefit = mapperApplicationBenefit.toDomain(benefitResponseDto);

        Consumption consumption = Consumption.builder()
                .consumptionAmount(Amount.create(
                        Currency.create(CurrencyEnum.ofValue(consumptionCurrency.currency()).orElseThrow(), consumptionCurrency.exchangeRate()),
                        cardProcessConsumptionCommand.amount()))
                .cardId(CardId.create(cardProcessConsumptionCommand.cardId()))
                .sellerName(cardProcessConsumptionCommand.sellerName())
                .build();

        card.consumption(balance, benefit, consumption);

        var consumptionRequestDto = mapperApplicationConsumption.toDto(consumption);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);
        var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);

        this.consumptionDBSavePort.save(consumptionRequestDto).orElseThrow(() -> new ApplicationConsumptionException(FAILED_TO_CREATE_CONSUMPTION));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));
        this.benefitDBSavePort.save(benefitRequestDto).orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_UPDATE_BENEFIT));

        return consumption.getId();
    }
}
