package com.bank.credit_bank.infrastructure.ws.dto;

import java.math.BigDecimal;

public record CurrencyDto(BigDecimal value, String currency) {
}
