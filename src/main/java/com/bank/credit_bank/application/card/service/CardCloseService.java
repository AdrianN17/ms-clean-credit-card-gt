package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.benefit.BusinessServiceBenefit;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.port.in.CardCloseUseCase;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

public class CardCloseService implements CardCloseUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;

    public CardCloseService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceBenefit businessServiceBenefit) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
    }

    @Override
    public CardId execute(CardCloseCommand cardCloseCommand) {

        var card = businessServiceCard.get(cardCloseCommand.cardId());
        var balance = businessServiceBalance.get(cardCloseCommand.cardId());
        var benefit = businessServiceBenefit.get(cardCloseCommand.cardId());

        card.close();
        balance.close();
        benefit.close();

        var id = businessServiceCard.save(card);
        businessServiceBalance.save(balance);
        businessServiceBenefit.save(benefit);

        return id;
    }
}
