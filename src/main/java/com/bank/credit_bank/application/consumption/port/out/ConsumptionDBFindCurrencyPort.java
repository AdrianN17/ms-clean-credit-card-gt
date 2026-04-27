package com.bank.credit_bank.application.consumption.port.out;

import java.util.Optional;
import java.util.UUID;

public interface ConsumptionDBFindCurrencyPort {
    Optional<String> load(UUID consumptionId, String cardId);
}
