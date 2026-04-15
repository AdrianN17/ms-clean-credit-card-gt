package com.bank.credit_bank.application.payment.port.out;

import com.bank.credit_bank.application.payment.queries.FindPaymentByDatesAndCardIdCriteria;
import com.bank.credit_bank.application.payment.view.LoadPaymentView;

import java.util.List;

@FunctionalInterface
public interface PaymentsFindByDatesAndCardIdPort {
    List<LoadPaymentView> load(FindPaymentByDatesAndCardIdCriteria criteria);
}
