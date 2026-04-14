package com.bank.credit_bank.domain.card.model.constants;

public interface CardErrorMessage {
    String AMOUNT_EXCEED_CREDIT_LIMIT = "El monto de consumo excede la linea de credito";
    String IN_DEBT_CARD = "No se puede pay una tarjeta que no esta en deuda";

    String PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT = "La categoria de pay no coincide con el monto a pay";
    String PAYMENT_CATEGORY_EXCEED_LIKE = "El monto de pay excede en ";

    String TYPE_CARD_CANNOT_BE_NULL = "Type card cannot be null";
    String CATEGORY_CARD_CANNOT_BE_NULL = "Category card cannot be null";
    String CREDIT_CANNOT_BE_NULL = "Credit cannot be null";
    String CREDIT_TOTAL_CANNOT_BE_NULL = "Credit total cannot be null";
    String DEBT_TAX_CANNOT_BE_NULL = "Debt tax cannot be null";
    String CURRENCY_CANNOT_BE_NULL = "Currency cannot be null";
    String EXCHANGE_RATE_CANNOT_BE_NULL = "Exchange rate cannot be null";
    String BALANCE_CANNOT_BE_NULL = "Balance cannot be null";
    String BENEFIT_CANNOT_BE_NULL = "Benefit cannot be null";
    String PAYMENT_DAY_CANNOT_BE_NULL = "Payment paymentDay cannot be null";

    String ID_CANNOT_BE_NULL = "ID cannot be null";
    String STATUS_CANNOT_BE_NULL = "Status cannot be null";
    String CARD_STATUS_CANNOT_BE_NULL = "Card status cannot be null";

}
