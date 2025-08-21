package com.areeba.cms.transaction.record;

import com.areeba.cms.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TransactionUpdateRecord(BigDecimal amount, TransactionType type, Timestamp date) {
}
