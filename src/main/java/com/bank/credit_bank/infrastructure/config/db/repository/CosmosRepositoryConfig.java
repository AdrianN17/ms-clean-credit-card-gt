package com.bank.credit_bank.infrastructure.config.db.repository;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("new")
@Configuration
@EnableCosmosRepositories(
        basePackages = "com.bank.credit_bank.infrastructure.db.nosql.cosmos.repository",
        considerNestedRepositories = true
)
public class CosmosRepositoryConfig {

}
