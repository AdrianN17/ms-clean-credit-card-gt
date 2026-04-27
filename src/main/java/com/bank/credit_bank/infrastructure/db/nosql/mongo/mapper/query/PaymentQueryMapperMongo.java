package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query;

import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.PaymentEntityMongo;

@FunctionalInterface
public interface PaymentQueryMapperMongo {
    LoadPaymentView toView(PaymentEntityMongo entity);
}
