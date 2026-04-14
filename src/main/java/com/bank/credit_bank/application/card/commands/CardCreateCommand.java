package com.bank.credit_bank.application.card.commands;

import java.math.BigDecimal;

public record CardCreateCommand(Integer typeCard,
                                Integer categoryCard,
                                BigDecimal creditTotal,
                                BigDecimal debtTax,
                                Boolean hasDiscount,
                                BigDecimal multiplierPoints,
                                Short paymentDay,
                                Integer currency) {
}
