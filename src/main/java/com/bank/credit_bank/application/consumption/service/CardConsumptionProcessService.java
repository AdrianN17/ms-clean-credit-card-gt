package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.benefit.BusinessServiceBenefit;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.business.consumption.BusinessServiceConsumption;
import com.bank.credit_bank.application.consumption.commands.CardProcessConsumptionCommand;
import com.bank.credit_bank.application.consumption.exceptions.ApplicationConsumptionException;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionProcessUseCase;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import static com.bank.credit_bank.application.consumption.constants.ConsumptionApplicationErrorMessage.CONSUMPTION_CURRENCY_NOT_FOUND;

public class CardConsumptionProcessService implements ConsumptionProcessUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;
    private final BusinessServiceConsumption businessServiceConsumption;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final LoadCurrencyPort loadCurrencyPort;

    public CardConsumptionProcessService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceBenefit businessServiceBenefit, BusinessServiceConsumption businessServiceConsumption, MapperApplicationCurrency mapperApplicationCurrency, LoadCurrencyPort loadCurrencyPort) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
        this.businessServiceConsumption = businessServiceConsumption;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.loadCurrencyPort = loadCurrencyPort;
    }

    @Override
    public ConsumptionId execute(CardProcessConsumptionCommand cardProcessConsumptionCommand) {

        var card = businessServiceCard.get(cardProcessConsumptionCommand.cardId());
        var balance = businessServiceBalance.get(cardProcessConsumptionCommand.cardId());
        var benefit = businessServiceBenefit.get(cardProcessConsumptionCommand.cardId());

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

        var id = businessServiceConsumption.save(consumption);
        businessServiceBalance.save(balance);
        businessServiceBenefit.save(benefit);
        businessServiceCard.save(card);

        return id;
    }
}
