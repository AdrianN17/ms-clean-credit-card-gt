package com.bank.credit_bank.application.payment.service;
import com.bank.credit_bank.application.payment.port.in.PaymentFindByDatesAndCardIdQuery;
import com.bank.credit_bank.application.payment.port.out.PaymentsFindByDatesAndCardIdPort;
import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;

import java.util.List;

public class PaymentFindByDatesAndCardIdService implements PaymentFindByDatesAndCardIdQuery {
    private final PaymentsFindByDatesAndCardIdPort paymentsFindByDatesAndCardIdPort;

    public PaymentFindByDatesAndCardIdService(PaymentsFindByDatesAndCardIdPort paymentsFindByDatesAndCardIdPort) {
        this.paymentsFindByDatesAndCardIdPort = paymentsFindByDatesAndCardIdPort;
    }

    @Override
    public List<LoadPaymentView> execute(FindPaymentByDatesAndCardIdCriteria criteria) {
        return this.paymentsFindByDatesAndCardIdPort.load(criteria);
    }
}
