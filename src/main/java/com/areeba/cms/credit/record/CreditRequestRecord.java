package com.areeba.cms.credit.record;

import com.areeba.cms.enums.Currency;

import java.math.BigDecimal;

public record CreditRequestRecord(String cardNumber, BigDecimal amount, Currency currency) {
}
