package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query;

import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.PaymentEntityCosmos;

@FunctionalInterface
public interface PaymentQueryMapperCosmos {
    LoadPaymentView toView(PaymentEntityCosmos entity);
}
