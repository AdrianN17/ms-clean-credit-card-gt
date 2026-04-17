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
import com.bank.credit_bank.domain.base.enums.CurrencyEnum;
import com.bank.credit_bank.domain.base.vo.Amount;
import com.bank.credit_bank.domain.base.vo.Currency;
import com.bank.credit_bank.domain.benefit.model.vo.Point;
import com.bank.credit_bank.domain.card.model.enums.CategoryPaymentEnum;
import com.bank.credit_bank.domain.card.model.vo.cardId.CardId;
import com.bank.credit_bank.domain.payment.model.entities.Payment;
import com.bank.credit_bank.domain.payment.model.enums.ChannelPaymentEnum;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

import static com.bank.credit_bank.application.payment.constants.PaymentApplicationErrorMessage.PAYMENT_CURRENCY_NOT_FOUND;
import static java.util.Objects.isNull;

public class CardPaymentProcessService implements PaymentProcessUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServiceBenefit businessServiceBenefit;
    private final BusinessServicePayment businessServicePayment;
    private final MapperApplicationCurrency mapperApplicationCurrency;
    private final LoadCurrencyPort loadCurrencyPort;

    public CardPaymentProcessService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServiceBenefit businessServiceBenefit, BusinessServicePayment businessServicePayment, MapperApplicationCurrency mapperApplicationCurrency, LoadCurrencyPort loadCurrencyPort) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServiceBenefit = businessServiceBenefit;
        this.businessServicePayment = businessServicePayment;
        this.mapperApplicationCurrency = mapperApplicationCurrency;
        this.loadCurrencyPort = loadCurrencyPort;
    }


    @Override
    public PaymentId execute(CardProcessPaymentCommand cardProcessPaymentCommand) {

        var card = businessServiceCard.get(cardProcessPaymentCommand.cardId());
        var balance = businessServiceBalance.get(cardProcessPaymentCommand.cardId());

        var paymentCurrencyDto = loadCurrencyPort.load(cardProcessPaymentCommand.currency())
                .orElseThrow(() -> new ApplicationPaymentException(PAYMENT_CURRENCY_NOT_FOUND));

        var paymentCurrency = mapperApplicationCurrency.toDtoRequest(paymentCurrencyDto);

        var payment = Payment.builder()
                .paymentAmount(Amount.create(Currency.create(CurrencyEnum.ofValue(paymentCurrency.currency()).orElseThrow(), paymentCurrency.exchangeRate()), cardProcessPaymentCommand.amount()))
                .category(CategoryPaymentEnum.ofValue(cardProcessPaymentCommand.category()).orElseThrow())
                .cardId(CardId.create(cardProcessPaymentCommand.cardId()))
                .channelPayment(ChannelPaymentEnum.ofValue(cardProcessPaymentCommand.channelPayment()).orElseThrow())
                .build();

        var paymentAmount = payment.getPaymentAmount();

        if (!isNull(cardProcessPaymentCommand.pointsUsed())) {
            var benefit = businessServiceBenefit.get(cardProcessPaymentCommand.cardId());

            var point = Point.create(cardProcessPaymentCommand.pointsUsed());
            paymentAmount = benefit.discount(paymentAmount, point);

            businessServiceBenefit.save(benefit);
        }

        balance.payBalance(paymentAmount, payment.getCategory(), payment.getPaymentApprobation());
        card.updateStatus(balance.isOvercharged());

        var id = businessServicePayment.save(payment);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);

        return id;
    }
}
