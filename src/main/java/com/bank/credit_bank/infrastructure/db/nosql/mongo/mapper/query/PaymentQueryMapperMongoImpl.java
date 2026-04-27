package com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query;

import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.entity.PaymentEntityMongo;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueryMapperMongoImpl implements PaymentQueryMapperMongo {

    @Override
    public LoadPaymentView toView(PaymentEntityMongo entity) {
        return new LoadPaymentView(
                entity.getPaymentId(),
                entity.getAmount(),
                entity.getCurrency().getCode(),
                entity.getPaymentDate(),
                entity.getPaymentApprobationDate(),
                entity.getCategory().getCode(),
                entity.getCardId(),
                entity.getChannel().getCode()
        );
    }
}
