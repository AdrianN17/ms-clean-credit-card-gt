package com.bank.credit_bank.application.consumption.queries;

import java.time.LocalDate;

public record FindConsumptionByDatesAndCardIdCriteria(LocalDate start, LocalDate end, Long cardId) {

}
