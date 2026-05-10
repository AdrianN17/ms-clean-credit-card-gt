package com.bank.credit_bank.infrastructure.config.db.card.infrastructure;

import com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter.CardJpaRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardAccountPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardAccountPersistanceMapperImpl;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.CardPersistanceMapperImpl;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.query.CardQueryMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.CardAccountJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.CardJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.CardVOJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CardSqlServerConfig {

    @Bean
    public CardJpaRepositoryAdapter cardJpaRepositoryAdapter(CardJpaRepository cardJpaRepository,
                                                             CardVOJpaRepository cardVOJpaRepository,
                                                             CardPersistanceMapper cardPersistanceMapper,
                                                             CardAccountJpaRepository cardAccountJpaRepository,
                                                             CardAccountPersistanceMapper cardAccountPersistanceMapper,
                                                             CardQueryMapper cardQueryMapper) {
        return new CardJpaRepositoryAdapter(cardJpaRepository,
                cardVOJpaRepository,
                cardPersistanceMapper,
                cardAccountJpaRepository,
                cardAccountPersistanceMapper,
                cardQueryMapper);
    }

    @Bean
    public CardPersistanceMapper cardPersistanceMapper() {
        return new CardPersistanceMapperImpl();
    }

    @Bean
    public CardAccountPersistanceMapper cardAccountPersistanceMapper() {
        return new CardAccountPersistanceMapperImpl();
    }
}
