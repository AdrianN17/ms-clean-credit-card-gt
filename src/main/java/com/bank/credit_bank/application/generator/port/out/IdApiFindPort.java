package com.bank.credit_bank.application.generator.port.out;

import java.util.Optional;

@FunctionalInterface
public interface IdApiFindPort {
    Optional<Long> load();
}
