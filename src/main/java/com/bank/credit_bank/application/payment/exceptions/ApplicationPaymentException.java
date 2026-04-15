package com.bank.credit_bank.application.payment.exceptions;

import com.bank.credit_bank.application.generic.exceptions.ApplicationException;

public class ApplicationPaymentException
        extends ApplicationException {

    public ApplicationPaymentException(String message) {
        super(message);
    }
}
