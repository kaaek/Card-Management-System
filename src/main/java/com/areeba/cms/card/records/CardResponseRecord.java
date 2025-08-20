package com.areeba.cms.card.records;

import com.areeba.cms.enums.Status;

import java.util.Date;
import java.util.UUID;

public record CardResponseRecord(UUID id, Status status, Date expiry, String cardNumber) {
}
