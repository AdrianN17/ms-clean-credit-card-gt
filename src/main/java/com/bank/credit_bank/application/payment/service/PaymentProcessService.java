package com.bank.credit_bank.application.payment.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.benefit.BusinessServiceBenefit;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.business.payment.BusinessServicePayment;
import com.bank.credit_bank.application.currency.mapper.MapperApplicationCurrency;
import com.bank.credit_bank.application.currency.port.out.LoadCurrencyPort;
import com.bank.credit_bank.application.payment.commands.CardProcessPaymentCommand;
import com.bank.credit_bank.application.payment.exceptions.ApplicationPaymentException;
import com.bank.credit_bank.application.payment.port.in.PaymentProcessUseCase;
import com.bank.credit_bank.domain.balance.model.entities.BalancePago;
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.entities.Card;
import com.bank.credit_bank.domain.payment.model.factory.PaymentFactory;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.PAYMENT_CURRENCY_NOT_FOUND;
import static java.util.Objects.isNull;

public class PaymentProcessService implements PaymentProcessUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;
    private final BusinessServicePayment businessServicePayment;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final LoadCurrencyPort loadCurrencyPort;
    private final PaymentFactory paymentFactory;

    public PaymentProcessService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceBenefit businessServiceBenefit, BusinessServicePayment businessServicePayment, MapperApplicationCurrency mapperApplicationCurrency, LoadCurrencyPort loadCurrencyPort, PaymentFactory paymentFactory) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
        this.businessServicePayment = businessServicePayment;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.loadCurrencyPort = loadCurrencyPort;
        this.paymentFactory = paymentFactory;
    }

    @Override
    public PaymentId execute(CardProcessPaymentCommand cardProcessPaymentCommand) {

        var card = businessServiceCard.get(cardProcessPaymentCommand.cardId());
        var balance = BalancePago.from(businessServiceBalance.get(cardProcessPaymentCommand.cardId()));

        var paymentCurrencyDto = loadCurrencyPort.load(cardProcessPaymentCommand.currency())
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var paymentCurrency = mapperApplicationCurrency.toDtoRequest(paymentCurrencyDto);

        var paymentAmount = Amount.create(
                Currency.create(
                        CurrencyEnum.ofValue(paymentCurrency.currency()).orElseThrow(),
                        paymentCurrency.exchangeRate()),
                cardProcessPaymentCommand.amount()
        );

        if (!isNull(cardProcessPaymentCommand.pointsUsed()))
            return paymentProcessWithBenefit(cardProcessPaymentCommand, paymentAmount, card, balance);
        else
            return paymentProcessNoBenefit(cardProcessPaymentCommand, paymentAmount, card, balance);
    }

    private PaymentId paymentProcessWithBenefit(CardProcessPaymentCommand cardProcessPaymentCommand,
                                                Amount paymentAmount,
                                                Card card, BalancePago balance) {

        var benefit = businessServiceBenefit.get(cardProcessPaymentCommand.cardId());

        var point = Point.create(cardProcessPaymentCommand.pointsUsed());
        paymentAmount = benefit.discount(paymentAmount, point);

        var payment = paymentFactory.create(
                paymentAmount.getCurrency().getCurrency().getValue(),
                paymentAmount.getCurrency().getExchangeRate(),
                paymentAmount.getAmount(),
                cardProcessPaymentCommand.category(),
                cardProcessPaymentCommand.cardId(),
                cardProcessPaymentCommand.channelPayment());

        payment.validateIfPaymentIsPossible(balance.getAvailable(), balance.getTotal(), balance.getDateRange());
        balance.apply(paymentAmount);
        card.updateStatus(balance.isOvercharged());

        var id = businessServicePayment.save(payment);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);
        businessServiceBenefit.save(benefit);

        return id;
    }

    private PaymentId paymentProcessNoBenefit(CardProcessPaymentCommand cardProcessPaymentCommand,
                                              Amount paymentAmount,
                                              Card card, BalancePago balance) {

        var payment = paymentFactory.create(
                paymentAmount.getCurrency().getCurrency().getValue(),
                paymentAmount.getCurrency().getExchangeRate(),
                paymentAmount.getAmount(),
                cardProcessPaymentCommand.category(),
                cardProcessPaymentCommand.cardId(),
                cardProcessPaymentCommand.channelPayment());

        payment.validateIfPaymentIsPossible(balance.getAvailable(), balance.getTotal(), balance.getDateRange());
        balance.apply(paymentAmount);
        card.updateStatus(balance.isOvercharged());

        var id = businessServicePayment.save(payment);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);

        return id;
    }

}
