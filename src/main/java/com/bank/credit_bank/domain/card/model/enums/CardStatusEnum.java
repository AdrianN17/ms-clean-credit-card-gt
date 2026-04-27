package com.bank.credit_bank.domain.card.model.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum CardStatusEnum {
    OPERATIVE("OPERATIVA", 1),
    OVERCHARGE("SOBREGIRADA", 2),
    IN_DEBT("DEUDOR", 3);

    private final String code;
    private final int value;

    CardStatusEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public static Optional<CardStatusEnum> ofValue(Integer value) {
        if (value == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(c -> c.value == value)
                .findFirst();
    }

    public static Optional<CardStatusEnum> ofCode(String code) {
        if (code == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(c -> Objects.equals(c.code, code))
                .findFirst();
    }
}
