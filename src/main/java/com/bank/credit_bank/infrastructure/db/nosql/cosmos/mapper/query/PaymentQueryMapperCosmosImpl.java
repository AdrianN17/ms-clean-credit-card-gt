package com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query;

import com.bank.credit_bank.application.payment.view.LoadPaymentView;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.entity.PaymentEntityCosmos;
import org.springframework.stereotype.Component;

@Component
public class PaymentQueryMapperCosmosImpl implements PaymentQueryMapperCosmos {

    @Override
    public LoadPaymentView toView(PaymentEntityCosmos entity) {
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
