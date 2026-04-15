package com.bank.credit_bank.application.currency.dto.request;

import java.math.BigDecimal;

public record CurrencyRequestDto(Integer currency,
                                 BigDecimal exchangeRate) {
}
