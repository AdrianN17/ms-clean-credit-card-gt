package com.bank.credit_bank.infrastructure.config.db.consumption.infrastructure;

import com.bank.credit_bank.infrastructure.db.nosql.mongo.adapter.ConsumptionMongoRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.ConsumptionPersistanceMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.persistance.ConsumptionPersistanceMapperMongoImpl;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.ConsumptionQueryMapperMongo;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.mapper.query.ConsumptionQueryMapperMongoImpl;
import com.bank.credit_bank.infrastructure.db.nosql.mongo.repository.ConsumptionMongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumptionMongoConfig {

    @Bean
    ConsumptionMongoRepositoryAdapter consumptionMongoRepositoryAdapter(ConsumptionMongoRepository consumptionMongoRepository,
                                                                        ConsumptionPersistanceMapperMongo consumptionPersistanceMapperMongo,
                                                                        ConsumptionQueryMapperMongo consumptionQueryMapperMongo) {
        return new ConsumptionMongoRepositoryAdapter(consumptionMongoRepository,
                consumptionPersistanceMapperMongo,
                consumptionQueryMapperMongo);
    }

    @Bean
    ConsumptionPersistanceMapperMongo consumptionPersistanceMapperMongo() {
        return new ConsumptionPersistanceMapperMongoImpl();
    }

    @Bean
    ConsumptionQueryMapperMongo consumptionQueryMapperMongo() {
        return new ConsumptionQueryMapperMongoImpl();
    }
}