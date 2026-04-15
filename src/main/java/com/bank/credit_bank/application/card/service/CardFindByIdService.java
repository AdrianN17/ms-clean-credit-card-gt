package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.port.in.CardFindByIdQuery;
import com.bank.credit_bank.application.card.port.out.CardBalanceBenefitFindByIdPort;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;

import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_BALANCE_BENEFIT_NOT_FOUND;

public class CardFindByIdService implements CardFindByIdQuery {

    private final CardBalanceBenefitFindByIdPort cardBalanceBenefitFindByIdPort;

    public CardFindByIdService(CardBalanceBenefitFindByIdPort cardBalanceBenefitFindByIdPort) {
        this.cardBalanceBenefitFindByIdPort = cardBalanceBenefitFindByIdPort;
    }

    @Override
    public LoadCardBalanceBenefitView execute(Long cardId) {
        return cardBalanceBenefitFindByIdPort
                .loadAll(cardId)
                .orElseThrow(() ->
                        new ApplicationCardException(CARD_BALANCE_BENEFIT_NOT_FOUND));
    }
}
