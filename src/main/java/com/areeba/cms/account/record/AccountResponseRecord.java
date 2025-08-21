package com.areeba.cms.account.record;

import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.Status;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponseRecord(UUID id, Status status, BigDecimal balance, Currency currency) {
}
