package com.bank.credit_bank.domain.benefit.model.exceptions;

import com.bank.credit_bank.domain.generic.exceptions.EntityException;

public class BenefitIdException extends EntityException {

    public BenefitIdException() {
    }

    public BenefitIdException(String message) {
        super(message);
    }
}

