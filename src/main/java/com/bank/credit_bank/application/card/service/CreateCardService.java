package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.businesscurrency.BusinessServiceBalance;
import com.bank.credit_bank.application.businesscurrency.BusinessServiceBenefit;
import com.bank.credit_bank.application.businesscurrency.BusinessServiceCard;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.in.CardCreateUseCase;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.exceptions.ApplicationGeneratorException;
import com.bank.credit_bank.application.generator.port.out.GenericEventPublisherPort;
import com.bank.credit_bank.application.generator.port.out.IdGeneratePort;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_CREATE_BALANCE;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_CREATE_BENEFIT;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.FAILED_TO_CREATE_CARD;
import static com.bank.credit_bank.application.generator.constants.GeneratorApplicationErrorMessage.*;

public class CreateCardService implements CardCreateUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;
    private final IdGeneratePort idGeneratePort;
    private final LoadCurrencyPort loadCurrencyPort;

    public CreateCardService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceBenefit businessServiceBenefit, IdGeneratePort idGeneratePort, LoadCurrencyPort loadCurrencyPort) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
        this.idGeneratePort = idGeneratePort;
        this.loadCurrencyPort = loadCurrencyPort;
    }


    @Override
    public CardId execute(CardCreateCommand cardCreateCommand) {

        Long idCard = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ID_GENERATION_ERROR));
        Long idCardAccount = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ACCOUNT_ID_GENERATION_ERROR));
        Long idBenefit = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(BENEFIT_ID_GENERATION_ERROR));
        Long idBalance = idGeneratePort.load().orElseThrow(() -> new ApplicationGeneratorException(BALANCE_ID_GENERATION_ERROR));

        var currencyResponseDto = loadCurrencyPort.load(cardCreateCommand.currency())
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var card = cardFactory.create(
                idCard,
                cardCreateCommand.typeCard(),
                cardCreateCommand.categoryCard(),
                idCardAccount,
                cardCreateCommand.paymentDay(),
                cardCreateCommand.creditTotal(),
                cardCreateCommand.debtTax(),
                currencyResponseDto.currency(),
                currencyResponseDto.exchangeRate()
        );

        var balance = balanceFactory.create(
                idBalance,
                idCard,
                cardCreateCommand.creditTotal(),
                cardCreateCommand.paymentDay(),
                currencyResponseDto.currency(),
                currencyResponseDto.exchangeRate()
        );

        var benefit = benefitFactory.create(
                idBenefit,
                idCard,
                cardCreateCommand.hasDiscount(),
                cardCreateCommand.multiplierPoints()
        );


        var id = businessServiceCard.save(card);
        businessServiceBalance.save(balance);
        businessServiceBenefit.save(benefit);

        return id;
    }
}
