package com.bank.credit_bank.infrastructure.config.db.payment.infrastructure;

import com.bank.credit_bank.infrastructure.db.nosql.cosmos.adapter.PaymentCosmosRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.PaymentPersistanceMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.PaymentPersistanceMapperCosmosImpl;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.PaymentQueryMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.PaymentQueryMapperCosmosImpl;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.repository.PaymentCosmosRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentCosmosConfig {

    @Bean
    public PaymentCosmosRepositoryAdapter paymentCosmosRepositoryAdapter(PaymentCosmosRepository paymentCosmosRepository,
                                                                         PaymentPersistanceMapperCosmos paymentPersistanceMapperCosmos,
                                                                         PaymentQueryMapperCosmos paymentQueryMapperCosmos) {
        return new PaymentCosmosRepositoryAdapter(paymentCosmosRepository,
                paymentPersistanceMapperCosmos,
                paymentQueryMapperCosmos);
    }

    @Bean
    public PaymentPersistanceMapperCosmos paymentPersistanceMapperCosmos() {
        return new PaymentPersistanceMapperCosmosImpl();
    }

    @Bean
    public PaymentQueryMapperCosmos paymentQueryMapperCosmos() {
        return new PaymentQueryMapperCosmosImpl();
    }
}
