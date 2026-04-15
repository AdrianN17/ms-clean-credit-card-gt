package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.in.CardCreateUseCase;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.generator.exceptions.ApplicationGeneratorException;
import com.bank.credit_bank.application.generator.port.out.IdApiFindPort;
import com.bank.credit_bank.domain.balance.model.factory.BalanceFactory;
import com.bank.credit_bank.domain.benefit.model.factory.BenefitFactory;
import com.bank.credit_bank.domain.card.model.factory.CardFactory;
import com.bank.credit_bank.domain.card.model.vo.CardId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_CREATE_BALANCE;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_CREATE_BENEFIT;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.CARD_CURRENCY_NOT_FOUND;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.FAILED_TO_CREATE_CARD;
import static com.bank.credit_bank.application.generator.constants.GeneratorApplicationErrorMessage.*;

public class CreateCardService implements CardCreateUseCase {

    private final CardDBSavePort cardDBSavePort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final BenefitDBSavePort benefitDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final MapperApplicationBenefit mapperApplicationBenefit;
    private final CardFactory cardFactory;
    private final BalanceFactory balanceFactory;
    private final BenefitFactory benefitFactory;
    private final IdApiFindPort idApiFindPort;

    public CreateCardService(CardDBSavePort cardDBSavePort,
                             BalanceDBSavePort balanceDBSavePort,
                             BenefitDBSavePort benefitDBSavePort,
                             LoadCurrencyPort loadCurrencyPort,
                             MapperApplicationCurrency mapperApplicationCurrency, MapperApplicationCard mapperApplicationCard, MapperApplicationBalance mapperApplicationBalance, MapperApplicationBenefit mapperApplicationBenefit, CardFactory cardFactory, BalanceFactory balanceFactory, BenefitFactory benefitFactory, IdApiFindPort idApiFindPort) {
        this.cardDBSavePort = cardDBSavePort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.benefitDBSavePort = benefitDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.mapperApplicationBenefit = mapperApplicationBenefit;
        this.cardFactory = cardFactory;
        this.balanceFactory = balanceFactory;
        this.benefitFactory = benefitFactory;
        this.idApiFindPort = idApiFindPort;
    }

    @Override
    public CardId execute(CardCreateCommand cardCreateCommand) {

        Long idCard = idApiFindPort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ID_GENERATION_ERROR));
        Long idCardAccount = idApiFindPort.load().orElseThrow(() -> new ApplicationGeneratorException(CARD_ACCOUNT_ID_GENERATION_ERROR));
        Long idBenefit = idApiFindPort.load().orElseThrow(() -> new ApplicationGeneratorException(BENEFIT_ID_GENERATION_ERROR));
        Long idBalance = idApiFindPort.load().orElseThrow(() -> new ApplicationGeneratorException(BALANCE_ID_GENERATION_ERROR));

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

        var cardRequestDto = mapperApplicationCard.toDto(card);


        var cardId = cardDBSavePort.save(cardRequestDto)
                .orElseThrow(() -> new ApplicationCardException(FAILED_TO_CREATE_CARD));

        var balance = balanceFactory.create(
                idBalance,
                idCard,
                cardCreateCommand.creditTotal(),
                cardCreateCommand.paymentDay(),
                currencyResponseDto.currency(),
                currencyResponseDto.exchangeRate()
        );


        var balanceRequestDto = mapperApplicationBalance.toDto(balance);

        var benefit = benefitFactory.create(
                idBenefit,
                idCard,
                cardCreateCommand.hasDiscount(),
                cardCreateCommand.multiplierPoints()
        );

        var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);

        balanceDBSavePort.save(balanceRequestDto)
                .orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_CREATE_BALANCE));

        benefitDBSavePort.save(benefitRequestDto)
                .orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_CREATE_BENEFIT));

        return cardId;
    }
}
