package com.bank.credit_bank.application.payment.service;

import com.bank.credit_bank.application.business.balance.BusinessServiceBalance;
import com.bank.credit_bank.application.business.card.BusinessServiceCard;
import com.bank.credit_bank.application.business.payment.BusinessServicePayment;
import com.bank.credit_bank.application.payment.commands.CardCancelPaymentCommand;
import com.bank.credit_bank.application.payment.port.in.PaymentCancelUseCase;
import com.bank.credit_bank.domain.payment.model.vo.PaymentId;

public class PaymentCancelService implements PaymentCancelUseCase {

    private final BusinessServiceCard businessServiceCard;
    private final BusinessServiceBalance businessServiceBalance;
    private final BusinessServicePayment businessServicePayment;

    public PaymentCancelService(BusinessServiceCard businessServiceCard, BusinessServiceBalance businessServiceBalance, BusinessServicePayment businessServicePayment) {
        this.businessServiceCard = businessServiceCard;
        this.businessServiceBalance = businessServiceBalance;
        this.businessServicePayment = businessServicePayment;
    }

    @Override
    public PaymentId execute(CardCancelPaymentCommand cardCancelPaymentCommand) {

        var card = businessServiceCard.get(cardCancelPaymentCommand.cardId());
        var balance = businessServiceBalance.get(cardCancelPaymentCommand.cardId());
        var payment = businessServicePayment.get(cardCancelPaymentCommand.cardId(), cardCancelPaymentCommand.paymentId());

        balance.cancelPayment(payment.getPaymentAmount());
        card.updateStatus(balance.isOvercharged());
        payment.close();

        var id = businessServicePayment.save(payment);
        businessServiceBalance.save(balance);
        businessServiceCard.save(card);

        return id;
    }
}
