package com.bank.credit_bank.infrastructure.config.db.benefit.infrastructure;

import com.bank.credit_bank.infrastructure.db.sql.sqlserver.adapter.BenefitJpaRepositoryAdapter;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BenefitPersistanceMapper;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.mapper.persistance.BenefitPersistanceMapperImpl;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.BenefitJpaRepository;
import com.bank.credit_bank.infrastructure.db.sql.sqlserver.repository.vo.BenefitVOJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BenefitSqlServerConfig {

    @Bean
    public BenefitJpaRepositoryAdapter benefitJpaRepositoryAdapter(BenefitJpaRepository benefitJpaRepository,
                                                                   BenefitVOJpaRepository benefitVOJpaRepository,
                                                                   BenefitPersistanceMapper benefitPersistanceMapper) {
        return new BenefitJpaRepositoryAdapter(benefitJpaRepository, benefitVOJpaRepository, benefitPersistanceMapper);
    }

    @Bean
    public BenefitPersistanceMapper benefitPersistanceMapper() {
        return new BenefitPersistanceMapperImpl();
    }
}
