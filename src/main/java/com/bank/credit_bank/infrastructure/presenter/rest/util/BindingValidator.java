package com.bank.credit_bank.infrastructure.presenter.rest.util;

import com.bank.credit_bank.infrastructure.presenter.rest.exception.RequestValidationException;
import org.springframework.validation.BindingResult;

public final class BindingValidator {

    private BindingValidator() {
    }

    /**
     * Lanza {@link RequestValidationException} si el BindingResult contiene errores de validación.
     *
     * @param bindingResult resultado del binding de Spring
     */
    public static void validate(BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult);
        }
    }
}

