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
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
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


    private final BenefitDBSavePort benefitDBSavePort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final ConsumptionDBSavePort consumptionDBSavePort;
    private final MapperApplicationConsumption mapperApplicationConsumption;
    private final GenericEventPublisherPort genericEventPublisherPort;


    @Override
    public ConsumptionId execute(CardProcessConsumptionCommand cardProcessConsumptionCommand) {





        var consumptionCurrencyDto = loadCurrencyPort.load(cardProcessConsumptionCommand.currency())
                .orElseThrow(() -> new ApplicationConsumptionException(CONSUMPTION_CURRENCY_NOT_FOUND));


        var consumptionCurrency = mapperApplicationCurrency.toDtoRequest(consumptionCurrencyDto);





        var consumption = Consumption.builder()
                .consumptionAmount(Amount.create(
                        Currency.create(CurrencyEnum.ofValue(consumptionCurrency.currency()).orElseThrow(), consumptionCurrency.exchangeRate()),
                        cardProcessConsumptionCommand.amount()))
                .cardId(CardId.create(cardProcessConsumptionCommand.cardId()))
                .sellerName(cardProcessConsumptionCommand.sellerName())
                .build();

        benefit.accumulate(consumption.getConsumptionAmount(), card.getCategoryCard());
        balance.consumeBalance(consumption.getConsumptionAmount(), card.getCardStatus());
        card.updateStatus(balance.isOvercharged());

        var balanceRequestDto = mapperApplicationBalance.toDto(balance);
        var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);

        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));
        this.benefitDBSavePort.save(benefitRequestDto).orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_UPDATE_BENEFIT));

        card.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        balance.pullDomainEvents().forEach(genericEventPublisherPort::publish);
        benefit.pullDomainEvents().forEach(genericEventPublisherPort::publish);


        return consumption.getId();
    }
}
