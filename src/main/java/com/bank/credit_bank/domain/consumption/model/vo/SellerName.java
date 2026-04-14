package com.bank.credit_bank.domain.consumption.model.vo;

import com.bank.credit_bank.domain.consumption.model.exceptions.SellerNameException;

import static com.bank.credit_bank.domain.consumption.model.Constants.SellerNameErrorMessage.SELLER_NAME_CANNOT_BE_EMPTY;
import static com.bank.credit_bank.domain.consumption.model.Constants.SellerNameErrorMessage.SELLER_NAME_CANNOT_BE_NULL;
import static com.bank.credit_bank.domain.util.Validation.isNotConditional;
import static com.bank.credit_bank.domain.util.Validation.isNotNull;

public class SellerName {
    private final String value;

    private SellerName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SellerName create(String value) {
        isNotNull(value, new SellerNameException(SELLER_NAME_CANNOT_BE_NULL));
        isNotConditional(value.isBlank(), new SellerNameException(SELLER_NAME_CANNOT_BE_EMPTY));
        return new SellerName(value);
    }
}
