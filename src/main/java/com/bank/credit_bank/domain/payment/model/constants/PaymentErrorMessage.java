package com.bank.credit_bank.domain.payment.model.constants;

public interface PaymentErrorMessage {
    String PAYMENT_DAY_NOT_NULL = "El dia de pay no puede ser nulo";
    String PAYMENT_CATEGORY_NOT_NULL = "La categoria del pay no puede ser nula";
    String PAYMENT_AMOUNT_NOT_NULL = "El monto del pay no puede ser nulo";
    String PAYMENT_AMOUNT_NOT_ZERO = "El monto del pay no puede ser cero";
    String IDENTIFIER_ID_NOT_NULL = "El identificador del pay no puede ser nulo";
    String CARD_ID_NOT_NULL = "El identificador de la tarjeta del pay no puede ser nulo";
    String PAYMENT_IS_STILL_IN_APPROBATION = "The payment is still in approbation.";
    String CHANGE_PAYMENT_NOT_NULL = "The change payment cannot be null.";

    String POINTS_CANNOT_USED_WITH_PREPAY = "Points cannot be used with prepay";

    String NORMAL_PAYMENT_MUST_BE_MORE_THAN_MINIMUN = "The normal payment must be more than the minimum payment.";
    String NORMAL_PAYMENT_MUST_BE_LESS_THAN_TOTAL = "The normal payment must be less than the total payment.";
    String TOTAL_PAYMENT_MUST_BE_COMPLETED = "The total payment must be completed.";

    String PAYMENT_IT_NOT_NECCESARY = "The payment is not necessary.";
    String PAYMENT_CATEGORY_NOT_SAME_AS_PAYMENT = "The payment category must be the same as the payment type.";

    String MINIMUN_PAYMENT_MUST_BE_THIRD_PART_OF_AVAILABLE = "The minimum payment must be a third part of the available balance.";

}
