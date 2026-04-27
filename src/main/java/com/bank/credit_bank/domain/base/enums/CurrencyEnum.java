package com.bank.credit_bank.domain.base.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum CurrencyEnum {
    PEN("PEN", 1),
    USD("USD", 2);

    private final String code;
    private final int value;

    CurrencyEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public static Optional<CurrencyEnum> ofValue(Integer value) {
        if (value == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(c -> c.value == value)
                .findFirst();
    }

    public static Optional<CurrencyEnum> ofCode(String code) {
        if (code == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(c -> Objects.equals(c.code, code))
                .findFirst();
    }
}
