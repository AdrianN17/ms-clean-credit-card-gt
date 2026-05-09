package com.bank.credit_bank.domain.generic.aggregate;

import com.bank.credit_bank.domain.base.enums.StatusEnum;
import com.bank.credit_bank.domain.generic.domain.GenericDomain;

import java.time.LocalDateTime;

public abstract class AggregateRoot<T> extends GenericDomain<T> {

    protected AggregateRoot(T id, StatusEnum status, LocalDateTime createdDate, LocalDateTime updatedDate) {
        super(id, status, createdDate, updatedDate);
    }

    protected Boolean isDeleted() {
        return false;
    }

}
