package com.bank.credit_bank.domain.base.exceptions;

import com.bank.credit_bank.domain.generic.exceptions.EntityException;


public class ApprobationException extends EntityException {

    public ApprobationException() {
    }

    public ApprobationException(String message) {
        super(message);
    }

    public ApprobationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApprobationException(Throwable cause) {
        super(cause);
    }

    public ApprobationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

