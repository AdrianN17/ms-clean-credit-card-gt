package com.bank.credit_bank.application.payment.port.in;

import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;

import java.util.List;

@FunctionalInterface
public interface PaymentFindByDatesAndCardIdQuery {
    List<LoadPaymentView> execute(FindPaymentByDatesAndCardIdCriteria criteria);
}
