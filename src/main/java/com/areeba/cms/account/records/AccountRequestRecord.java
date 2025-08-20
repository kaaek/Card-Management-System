package com.areeba.cms.account.records;

import com.areeba.cms.enums.Currency;

import java.math.BigDecimal;

public record AccountRequestRecord(BigDecimal balance, Currency currency) {

    // TODO: Override the canonical constructor to add validation logic (check create account method in the service.)

}
