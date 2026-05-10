package com.bank.credit_bank.infrastructure.config.generator;

import com.bank.credit_bank.infrastructure.generator.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeGeneratorConfig {

    @Bean
    public SnowflakeGenerator snowflakeGenerator() {
        return new SnowflakeGenerator();
    }
}
