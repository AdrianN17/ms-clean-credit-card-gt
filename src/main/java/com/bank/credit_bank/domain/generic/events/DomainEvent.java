package com.bank.credit_bank.domain.generic.events;

public interface DomainEvent {

    String eventType();

    default String systemName() {
        return "ms-clean-credit-bank (cb)";
    }

    ;

    default String version() {
        return "v1";
    }
}
