package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.benefit.BusinessServiceBenefit;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.port.in.CardCreateUseCase;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.exceptions.ApplicationGeneratorException;
import com.bank.credit_bank.application.generator.port.out.IdGeneratePort;
import com.bank.credit_bank.domain.balance.model.factory.BalanceFactory;
import com.bank.credit_bank.domain.benefit.model.entities.Benefit;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.generator.constants.GeneratorApplicationErrorMessage.*;
import static com.bank.credit_bank.domain.balance.model.enums.BalanceType.CONSUMPTION;

public class CardCreateService implements CardCreateUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;
    private final IdGeneratePort idGeneratePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final BalanceFactory balanceFactory;

    public CardCreateService(BusinessServiceCard businessServiceCard,
                             BusinessServiceBalance businessServiceBalance,
                             BusinessServiceBenefit businessServiceBenefit,
                             IdGeneratePort idGeneratePort,
                             LoadCurrencyPort loadCurrencyPort, BalanceFactory balanceFactory) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
        this.idGeneratePort = idGeneratePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.balanceFactory = balanceFactory;
    }

    @Override
    public CardId execute(CardCreateCommand cardCreateCommand) {

        Long idCard = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ID_GENERATION_ERROR));
        Long idCardAccount = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ACCOUNT_ID_GENERATION_ERROR));
        Long idBenefit = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(BENEFIT_ID_GENERATION_ERROR));
        Long idBalance = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(BALANCE_ID_GENERATION_ERROR));

        var currencyResponseDto = loadCurrencyPort.load(cardCreateCommand.currency())
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var card = Card.builder()
                .cardId(idCard)
                .typeCard(cardCreateCommand.typeCard())
                .categoryCard(cardCreateCommand.categoryCard())
                .cardAccountId(idCardAccount)
                .paymentDay(cardCreateCommand.paymentDay())
                .credit(cardCreateCommand.creditTotal(),
                        cardCreateCommand.debtTax(),
                        currencyResponseDto.currency(),
                        currencyResponseDto.exchangeRate())
                .build();

        var balance = balanceFactory.create(idBalance,
                currencyResponseDto.currency(),
                currencyResponseDto.exchangeRate(),
                idCard,
                cardCreateCommand.creditTotal(),
                cardCreateCommand.paymentDay(),
                CONSUMPTION);

        var benefit = Benefit.builder()
                .benefitId(idBenefit)
                .cardId(idCard)
                .discountPolicy(cardCreateCommand.hasDiscount(), cardCreateCommand.multiplierPoints())
                .build();

        var id = businessServiceCard.save(card);
        businessServiceBalance.save(balance);
        businessServiceBenefit.save(benefit);

        return id;
    }
}
