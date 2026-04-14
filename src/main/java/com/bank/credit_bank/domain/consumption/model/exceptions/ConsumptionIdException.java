package com.bank.credit_bank.domain.consumption.model.exceptions;

import com.bank.credit_bank.domain.generic.exceptions.EntityException;

public class ConsumptionIdException extends EntityException {

    public ConsumptionIdException() {
    }

    public ConsumptionIdException(String message) {
        super(message);
    }

    public ConsumptionIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsumptionIdException(Throwable cause) {
        super(cause);
    }
}

