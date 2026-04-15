package com.bank.credit_bank.application.card.service;

import com.bank.credit_bank.application.balance.exceptions.ApplicationBalanceException;
import com.bank.credit_bank.application.balance.mapper.MapperApplicationBalance;
import com.bank.credit_bank.application.balance.port.out.BalanceDBFindByIdPort;
import com.bank.credit_bank.application.balance.port.out.BalanceDBSavePort;
import com.bank.credit_bank.application.benefit.exceptions.ApplicationBenefitException;
import com.bank.credit_bank.application.benefit.mapper.MapperApplicationBenefit;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBFindByIdPort;
import com.bank.credit_bank.application.benefit.port.out.BenefitDBSavePort;
import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.exceptions.ApplicationCardException;
import com.bank.credit_bank.application.card.mapper.MapperApplicationCard;
import com.bank.credit_bank.application.card.port.in.CardCloseUseCase;
import com.bank.credit_bank.application.card.port.out.CardDBFindCurrencyPort;
import com.bank.credit_bank.application.card.port.out.CardFindByIdPort;
import com.bank.credit_bank.application.card.port.out.CardDBSavePort;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.domain.card.model.vo.CardId;

import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.BALANCE_NOT_FOUND;
import static com.bank.credit_bank.application.balance.constants.BalanceApplicationErrorMessage.FAILED_TO_UPDATE_BALANCE;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.BENEFIT_NOT_FOUND;
import static com.bank.credit_bank.application.benefit.constants.BenefitApplicationErrorMessage.FAILED_TO_UPDATE_BENEFIT;
import static com.bank.credit_bank.application.card.constants.CardApplicationErrorMessage.*;

public class CardCloseService implements CardCloseUseCase {

    private final CardFindByIdPort cardFindByIdPort;
    private final BalanceDBFindByIdPort balanceDBFindByIdPort;
    private final BenefitDBFindByIdPort benefitDBFindByIdPort;
    private final BenefitDBSavePort benefitDBSavePort;
    private final BalanceDBSavePort balanceDBSavePort;
    private final CardDBSavePort cardDBSavePort;
    private final LoadCurrencyPort loadCurrencyPort;
    private final CardDBFindCurrencyPort cardDBFindCurrencyPort;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final MapperApplicationCard mapperApplicationCard;
    private final MapperApplicationBalance mapperApplicationBalance;
    private final MapperApplicationBenefit mapperApplicationBenefit;

    public CardCloseService(CardFindByIdPort cardFindByIdPort,
                            BalanceDBFindByIdPort balanceDBFindByIdPort,
                            BenefitDBFindByIdPort benefitDBFindByIdPort,
                            BenefitDBSavePort benefitDBSavePort,
                            BalanceDBSavePort balanceDBSavePort,
                            CardDBSavePort cardDBSavePort,
                            LoadCurrencyPort loadCurrencyPort,
                            CardDBFindCurrencyPort cardDBFindCurrencyPort,
                            MapperApplicationCurrency mapperApplicationCurrency,
                            MapperApplicationCard mapperApplicationCard,
                            MapperApplicationBalance mapperApplicationBalance,
                            MapperApplicationBenefit mapperApplicationBenefit) {
        this.cardFindByIdPort = cardFindByIdPort;
        this.balanceDBFindByIdPort = balanceDBFindByIdPort;
        this.benefitDBFindByIdPort = benefitDBFindByIdPort;
        this.benefitDBSavePort = benefitDBSavePort;
        this.balanceDBSavePort = balanceDBSavePort;
        this.cardDBSavePort = cardDBSavePort;
        this.loadCurrencyPort = loadCurrencyPort;
        this.cardDBFindCurrencyPort = cardDBFindCurrencyPort;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.mapperApplicationCard = mapperApplicationCard;
        this.mapperApplicationBalance = mapperApplicationBalance;
        this.mapperApplicationBenefit = mapperApplicationBenefit;
    }

    @Override
    public CardId execute(CardCloseCommand cardCloseCommand) {

        var cardCurrencyValue = cardDBFindCurrencyPort.load(cardCloseCommand.cardId())
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var cardCurrencyDto = loadCurrencyPort.load(cardCurrencyValue)
                .orElseThrow(() -> new ApplicationCardException(CARD_CURRENCY_NOT_FOUND));

        var cardCurrency = mapperApplicationCurrency.toDtoRequest(cardCurrencyDto);

        var cardResponseDto = cardFindByIdPort
                .load(cardCloseCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationCardException(CARD_NOT_FOUND));

        var balanceResponseDto = balanceDBFindByIdPort
                .load(cardCloseCommand.cardId(), cardCurrency)
                .orElseThrow(() -> new ApplicationBalanceException(BALANCE_NOT_FOUND));

        var benefitResponseDto = benefitDBFindByIdPort
                .load(cardCloseCommand.cardId())
                .orElseThrow(() -> new ApplicationBenefitException(BENEFIT_NOT_FOUND));

        var card = mapperApplicationCard.toDomain(cardResponseDto);
        var balance = mapperApplicationBalance.toDomain(balanceResponseDto);
        var benefit = mapperApplicationBenefit.toDomain(benefitResponseDto);

        card.close();
        balance.close();
        benefit.close();

        var cardRequestDto = mapperApplicationCard.toDto(card);
        var balanceRequestDto = mapperApplicationBalance.toDto(balance);
        var benefitRequestDto = mapperApplicationBenefit.toDto(benefit);

        this.cardDBSavePort.save(cardRequestDto).orElseThrow(() -> new ApplicationCardException(FAILED_TO_UPDATE_CARD));
        this.balanceDBSavePort.save(balanceRequestDto).orElseThrow(() -> new ApplicationBalanceException(FAILED_TO_UPDATE_BALANCE));
        this.benefitDBSavePort.save(benefitRequestDto).orElseThrow(() -> new ApplicationBenefitException(FAILED_TO_UPDATE_BENEFIT));

        return card.getId();
    }
}
