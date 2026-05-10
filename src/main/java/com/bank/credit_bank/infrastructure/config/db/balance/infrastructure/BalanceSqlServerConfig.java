package com.bank.credit_bank.infrastructure.config.db.balance.infrastructure;

import com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter.BalanceJpaRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BalancePersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BalancePersistanceMapperImpl;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.BalanceJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.BalanceVOJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceSqlServerConfig {

    @Bean
    BalanceJpaRepositoryAdapter balanceJpaRepositoryAdapter(BalanceJpaRepository balanceJpaRepository,
                                                            BalanceVOJpaRepository balanceVOJpaRepository,
                                                            BalancePersistanceMapper balancePersistenceMapper) {
        return new BalanceJpaRepositoryAdapter(balanceJpaRepository, balanceVOJpaRepository, balancePersistenceMapper);
    }

    @Bean
    BalancePersistanceMapper balancePersistanceMapper() {
        return new BalancePersistanceMapperImpl();
    }
}
