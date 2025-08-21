package com.areeba.cms.transaction.record;

import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionRequestRecord(BigDecimal amount, TransactionType type, Currency currency, String cardNumber) {
}
