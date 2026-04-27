package com.bank.credit_bank.application.payment.port.out;

import java.util.Optional;
import java.util.UUID;

public interface PaymentDBFindCurrencyPort {
    Optional<String> load(UUID paymentId, String cardId);
}
