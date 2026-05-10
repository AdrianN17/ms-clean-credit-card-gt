package com.bank.credit_bank.infrastructure.config.db.payment.infrastructure;

import com.bank.credit_bank.infrastructure.db.nosql.mongo.adapter.PaymentMongoRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.PaymentPersistanceMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.PaymentPersistanceMapperMongoImpl;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.PaymentQueryMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.PaymentQueryMapperMongoImpl;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.repository.PaymentMongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentMongoConfig {

    @Bean
    public PaymentMongoRepositoryAdapter paymentMongoRepositoryAdapter(PaymentMongoRepository paymentMongoRepository,
                                                                       PaymentPersistanceMapperMongo paymentPersistanceMapperMongo,
                                                                       PaymentQueryMapperMongo paymentQueryMapperMongo) {
        return new PaymentMongoRepositoryAdapter(paymentMongoRepository,
                paymentPersistanceMapperMongo,
                paymentQueryMapperMongo);
    }

    @Bean
    public PaymentPersistanceMapperMongo paymentPersistanceMapperMongo() {
        return new PaymentPersistanceMapperMongoImpl();
    }

    @Bean
    public PaymentQueryMapperMongo paymentQueryMapperMongo() {
        return new PaymentQueryMapperMongoImpl();
    }
}
