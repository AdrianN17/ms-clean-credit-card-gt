package com.bank.credit_bank.domain.generic.aggregate;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.generic.domain.GenericDomain;
import com.bank.credit_bank.domain.generic.events.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<T> extends GenericDomain<T> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(T id, StatusEnum status, LocalDateTime createdDate, LocalDateTime updatedDate) {
        super(id, status, createdDate, updatedDate);
    }


    protected void addEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    protected Boolean isDeleted() {
        return false;
    }

    ;

}
