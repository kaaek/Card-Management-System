package com.areeba.cms.debit.record;

import com.areeba.cms.enums.Currency;

import java.math.BigDecimal;

public record DebitRequestRecord(String cardNumber, BigDecimal amount, Currency currency) {
}
