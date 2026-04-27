package com.bank.credit_bank.infrastructure.presenter.rest.mapper;

import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.request.CardRequest;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.CardResponse;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.CardResponseAccount;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.CardResponseBalance;
import com.bank.credit_bank.infrastructure.presenter.rest.schema.response.CardResponseBenefit;

import java.util.Objects;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.CardMapperCommandMessageConstants.*;
import static com.bank.credit_bank.infrastructure.presenter.soap.constants.PaymentMapperCommandMessageConstants.PAYMENT_DATE_NOT_NULL;

public class CardApiMapperRequestCommandImpl implements CardApiMapperRequestCommand {

    @Override
    public CardCreateCommand toCommand(CardRequest request) {
        return new CardCreateCommand(
                request.getTypeCard(),
                request.getCategoryCard(),
                Objects.requireNonNull(request.getAccount(), ACCOUNT_NOT_NULL).getCreditTotal(),
                Objects.requireNonNull(request.getAccount(), ACCOUNT_NOT_NULL).getDebtTax(),
                Objects.requireNonNull(request.getBenefit(), BENEFIT_NOT_NULL).getHasDiscount(),
                Objects.requireNonNull(request.getBenefit(), BENEFIT_NOT_NULL).getMultiplierPoints(),
                Short.valueOf(
                        Objects.requireNonNull(
                                request.getAccount().getPaymentDate(), PAYMENT_DATE_NOT_NULL)),
                Objects.requireNonNull(request.getAccount().getCurrency(), CURRENCY_NOT_NULL)
        );
    }

    @Override
    public CardCloseCommand toCommandId(Long cardId) {
        return new CardCloseCommand(cardId);
    }

    @Override
    public CardResponse toResponse(LoadCardBalanceBenefitView view) {
        var cardResponseBenefit = new CardResponseBenefit(
                view.hasDiscount(),
                view.multiplierPoints(),
                view.totalPoint()
        );

        var cardResponseBalance = new CardResponseBalance(
                view.total(),
                view.old(),
                view.available(),
                view.startDate(),
                view.endDate()
        );

        var cardResponseAccount = new CardResponseAccount(
                view.creditTotal(),
                view.debtTax(),
                view.currency(),
                Objects.requireNonNull(view.paymentDate(), PAYMENT_DATE_NOT_NULL).toString(),
                view.cardStatus()
        );

        return new CardResponse(
                view.typeCard(),
                view.categoryCard(),
                cardResponseBenefit,
                cardResponseBalance,
                cardResponseAccount
        );
    }
}
