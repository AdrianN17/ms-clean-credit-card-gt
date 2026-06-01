package com.bank.credit_bank.infrastructure.config.db.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.bank.credit_bank.infrastructure.db.sql"
)
public class JpaRepositoryConfig {
}

