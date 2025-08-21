package com.areeba.cms.transaction.record;

import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record TransactionResponseRecord(UUID id, BigDecimal amount, Timestamp date, TransactionType type, Currency currency, UUID cardId) {
}
