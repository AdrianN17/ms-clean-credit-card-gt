package com.bank.credit_bank.application.consumption.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.business.consumption.BusinessServiceConsumption;
import com.bank.credit_bank.application.consumption.commands.CardCancelConsumptionCommand;
import com.bank.credit_bank.application.consumption.port.in.ConsumptionCancelUseCase;
import com.bank.credit_bank.domain.balance.model.entities.BalanceConsumo;
import com.bank.credit_bank.domain.consumption.model.vo.ConsumptionId;


public class ConsumptionCancelService implements ConsumptionCancelUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceConsumption businessServiceConsumption;

    public ConsumptionCancelService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceConsumption businessServiceConsumption) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceConsumption = businessServiceConsumption;
    }

    @Override
    public ConsumptionId execute(CardCancelConsumptionCommand cardCancelConsumptionCommand) {

        var card = businessServiceCard.get(cardCancelConsumptionCommand.cardId());
        var balance = BalanceConsumo.from(businessServiceBalance.get(cardCancelConsumptionCommand.cardId()));
        var consumption = businessServiceConsumption.get(cardCancelConsumptionCommand.cardId(),
                cardCancelConsumptionCommand.consumptionId());

        balance.cancel(consumption.getConsumptionAmount());
        card.updateStatus(balance.isOvercharged());
        consumption.close();

        var id = businessServiceConsumption.save(consumption);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);

        return id;
    }
}
