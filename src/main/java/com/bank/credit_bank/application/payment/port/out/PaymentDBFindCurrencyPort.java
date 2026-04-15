package com.bank.credit_bank.application.payment.port.out;

import java.util.Optional;
import java.util.UUID;

public interface PaymentDBFindCurrencyPort {
    Optional<Integer> load(UUID paymentId, String cardId);
}
