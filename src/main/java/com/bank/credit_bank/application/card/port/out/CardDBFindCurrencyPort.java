package com.bank.credit_bank.application.card.port.out;

import java.util.Optional;

public interface CardDBFindCurrencyPort {
    Optional<String> load(Long cardId);
}
