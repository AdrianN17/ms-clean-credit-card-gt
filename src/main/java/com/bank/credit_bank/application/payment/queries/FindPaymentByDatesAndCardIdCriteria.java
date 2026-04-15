package com.bank.credit_bank.application.payment.queries;

import java.time.LocalDate;

public record FindPaymentByDatesAndCardIdCriteria(LocalDate start, LocalDate end, Long cardId) {

}
