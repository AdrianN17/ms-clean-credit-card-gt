package com.bank.credit_bank.application.generator.port.out;

import java.util.Optional;

@FunctionalInterface
public interface IdGeneratePort {
    Optional<Long> load();
}
