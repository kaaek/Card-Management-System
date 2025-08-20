package com.areeba.cms.account.records;

import com.areeba.cms.enums.Status;

import java.math.BigDecimal;

public record AccountUpdateRecord(Status status, BigDecimal balance) {
}
