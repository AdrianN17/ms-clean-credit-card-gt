package com.bank.credit_bank.application.currency.dto.response;

import java.math.BigDecimal;

public record CurrencyResponseDto(String currency,
                                  BigDecimal exchangeRate) {
}
