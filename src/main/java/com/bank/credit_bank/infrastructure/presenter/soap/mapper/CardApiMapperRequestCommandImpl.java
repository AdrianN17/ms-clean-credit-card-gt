package com.bank.credit_bank.infrastructure.presenter.soap.mapper;

import com.bank.credit_bank.application.card.commands.CardCloseCommand;
import com.bank.credit_bank.application.card.commands.CardCreateCommand;
import com.bank.credit_bank.application.card.view.LoadCardBalanceBenefitView;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.ControlCardRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.request.InitiateCardRequest;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.AccountResponse;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.BalanceResponse;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.BenefitResponse;
import com.bank.credit_bank.infrastructure.presenter.soap.schema.response.CardResponse;

import java.util.Objects;

import static com.bank.credit_bank.infrastructure.presenter.soap.constants.CardMapperCommandMessageConstants.*;
import static com.bank.credit_bank.infrastructure.presenter.soap.util.SoapMapperResponse.toXMLGregorianCalendar;

public class CardApiMapperRequestCommandImpl implements CardApiMapperRequestCommand {

    @Override
    public CardCreateCommand toCommand(InitiateCardRequest request) {
        var data = request.getData();
        return new CardCreateCommand(
                data.getTypeCard(),
                data.getCategoryCard(),
                Objects.requireNonNull(data.getAccount(), ACCOUNT_NOT_NULL).getCreditTotal(),
                Objects.requireNonNull(data.getAccount(), ACCOUNT_NOT_NULL).getDebtTax(),
                Objects.requireNonNull(data.getBenefit(), BENEFIT_NOT_NULL).isHasDiscount(),
                Objects.requireNonNull(data.getBenefit(), BENEFIT_NOT_NULL).getMultiplierPoints(),
                Short.valueOf(Objects.requireNonNull(data.getAccount().getPaymentDate(), ACCOUNT_NOT_NULL)),
                Objects.requireNonNull(data.getAccount().getCurrency(), CURRENCY_NOT_NULL)
        );
    }

    @Override
    public CardCloseCommand toCommandId(ControlCardRequest parameters) {
        return new CardCloseCommand(parameters.getCardId());
    }

    @Override
    public CardResponse toResponse(LoadCardBalanceBenefitView view) {
        BenefitResponse benefitResponse = new BenefitResponse();
        benefitResponse.setHasDiscount(view.hasDiscount());
        benefitResponse.setMultiplierPoints(view.multiplierPoints());
        benefitResponse.setTotalPoint(view.totalPoint());

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setTotalAmount(view.total());
        balanceResponse.setOldAmount(view.old());
        balanceResponse.setAvailableAmount(view.available());
        balanceResponse.setStartDate(toXMLGregorianCalendar(view.startDate()));
        balanceResponse.setEndDate(toXMLGregorianCalendar(view.endDate()));

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setCreditTotal(view.creditTotal());
        accountResponse.setDebtTax(view.debtTax());
        accountResponse.setCurrency(view.currency());
        accountResponse.setPaymentDate(String.valueOf(view.paymentDate()));
        accountResponse.setCardStatus(view.cardStatus());

        CardResponse cardResponse = new CardResponse();
        cardResponse.setTypeCard(view.typeCard());
        cardResponse.setCategoryCard(view.categoryCard());
        cardResponse.setBenefit(benefitResponse);
        cardResponse.setBalance(balanceResponse);
        cardResponse.setAccount(accountResponse);

        return cardResponse;
    }
}

