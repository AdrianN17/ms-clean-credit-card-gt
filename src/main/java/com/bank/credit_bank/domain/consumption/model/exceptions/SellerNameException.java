package com.bank.credit_bank.domain.consumption.model.exceptions;

import com.bank.credit_bank.domain.generic.exceptions.EntityException;

public class SellerNameException extends EntityException {

    public SellerNameException() {
    }

    public SellerNameException(String message) {
        super(message);
    }

    public SellerNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SellerNameException(Throwable cause) {
        super(cause);
    }
}

