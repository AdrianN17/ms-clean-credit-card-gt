package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.business.consumption.BusinessServiceConsumption;
import com.bank.credit_bank.application.consumption.commands.CardSplitConsumptionCommand;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionSplitUseCase;
import com.bank.credit_bank.domain.consumption.model.entities.Consumption;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;

import java.util.List;

import static com.bank.credit_bank.domain.balance.model.enums.BalanceType.CONSUMPTION;

public class ConsumptionSplitService implements ConsumptionSplitUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceConsumption businessServiceConsumption;

    public ConsumptionSplitService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceConsumption businessServiceConsumption) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceConsumption = businessServiceConsumption;
    }

    @Override
    public List<ConsumptionId> execute(CardSplitConsumptionCommand cardSplitConsumptionCommand) {

        var card = businessServiceCard.get(cardSplitConsumptionCommand.cardId());
        var balance = businessServiceBalance.get(cardSplitConsumptionCommand.cardId(), CONSUMPTION);
        var consumption = businessServiceConsumption.get(cardSplitConsumptionCommand.cardId(),
                cardSplitConsumptionCommand.consumptionId());

        var consumptions = consumption.split(cardSplitConsumptionCommand.installments(), card.getCredit().getDebtTax());
        balance.cancel(consumption.getConsumptionAmount());
        card.validateIfCardIfInDebt();
        consumption.close();
        card.updateStatus(balance.isOvercharged());

        consumptions.stream()
                .map(Consumption::getConsumptionAmount)
                .forEach(amount -> {
                    balance.apply(amount);
                    card.updateStatus(balance.isOvercharged());
                });

        var consumptionIds = consumptions.stream()
                .map(businessServiceConsumption::save)
                .toList();

        businessServiceConsumption.save(consumption);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);

        return consumptionIds;
    }
}
