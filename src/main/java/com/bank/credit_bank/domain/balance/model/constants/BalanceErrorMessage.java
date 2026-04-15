package com.bank.credit_bank.domain.balance.model.constants;

public interface BalanceErrorMessage {
    String PAYMENT_CANNOT_BE_NULL = "Payment cannot be null";
    String CONSUMPTION_CANNOT_BE_NULL = "Consumption cannot be null";
    String TOTAL_AMOUNT_CANNOT_BE_NULL = "Total amount cannot be null";
    String DATE_RANGE_CANNOT_BE_NULL = "Date range cannot be null";
    String POINT_CANNOT_BE_NULL = "Point cannot be null";
    String POINTS_CANNOT_USED_WITH_PREPAY = "Points cannot be used with prepay";
    String AVAILABLE_AMOUNT_CANNOT_BE_NULL = "Available amount cannot be null";
    String OLD_AMOUNT_CANNOT_BE_NULL = "Old amount cannot be null";
    String CONSUMPTIONS_CANNOT_BE_NULL = "Consumptions cannot be null";
    String CONSUMPTIONS_CANNOT_BE_EMPTY = "Consumptions cannot be empty";
    String CARD_ID_CANNOT_BE_NULL = "Card ID cannot be null";
    String CURRENCY_CANNOT_BE_NULL = "Currency cannot be null";
    String EXCHANGE_RATE_CANNOT_BE_NULL = "Exchange rate cannot be null";
    String BALANCE_ID_CANNOT_BE_NULL = "Balance ID cannot be null";
    String BALANCE_ID_MUST_BE_NUMERIC = "Balance ID must be numeric";

    String ID_CANNOT_BE_NULL = "ID cannot be null";
    String PAYMENT_DAY_CANNOT_BE_NULL = "Payment day cannot be null";
}
