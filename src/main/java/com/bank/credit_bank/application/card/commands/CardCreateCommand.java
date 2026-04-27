package com.bank.credit_bank.application.card.commands;

import java.math.BigDecimal;

public record CardCreateCommand(String typeCard,
                                String categoryCard,
                                BigDecimal creditTotal,
                                BigDecimal debtTax,
                                Boolean hasDiscount,
                                BigDecimal multiplierPoints,
                                Short paymentDay,
                                String currency) {
}
