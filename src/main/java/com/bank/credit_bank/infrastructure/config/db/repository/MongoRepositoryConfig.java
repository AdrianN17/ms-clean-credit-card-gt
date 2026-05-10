package com.bank.credit_bank.infrastructure.config.db.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("old")
@Configuration
@EnableMongoRepositories(
        basePackages = "com.bank.credit_bank.infrastructure.db.nosql.mongo.repository",
        considerNestedRepositories = true
)
public class MongoRepositoryConfig {
}
