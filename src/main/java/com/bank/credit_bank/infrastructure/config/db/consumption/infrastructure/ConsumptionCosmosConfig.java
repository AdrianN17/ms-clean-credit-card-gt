package com.bank.credit_bank.infrastructure.config.db.consumption.infrastructure;

import com.bank.credit_bank.infrastructure.db.nosql.cosmos.adapter.ConsumptionCosmosRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.ConsumptionPersistanceMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.persistance.ConsumptionPersistanceMapperCosmosImpl;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.ConsumptionQueryMapperCosmos;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.mapper.query.ConsumptionQueryMapperCosmosImpl;
import com.bank.credit_bank.infrastructure.db.nosql.cosmos.repository.ConsumptionCosmosRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("new")
@Configuration
public class ConsumptionCosmosConfig {

    @Bean
    ConsumptionCosmosRepositoryAdapter consumptionCosmosRepositoryAdapter(ConsumptionCosmosRepository consumptionCosmosRepository,
                                                                          ConsumptionPersistanceMapperCosmos consumptionPersistanceMapperCosmos,
                                                                          ConsumptionQueryMapperCosmos consumptionQueryMapperCosmos) {
        return new ConsumptionCosmosRepositoryAdapter(consumptionCosmosRepository,
                consumptionPersistanceMapperCosmos,
                consumptionQueryMapperCosmos);
    }

    @Bean
    ConsumptionPersistanceMapperCosmos consumptionPersistanceMapperCosmos() {
        return new ConsumptionPersistanceMapperCosmosImpl();
    }

    @Bean
    ConsumptionQueryMapperCosmos consumptionQueryMapperCosmos() {
        return new ConsumptionQueryMapperCosmosImpl();
    }
}
