package com.areeba.cms.card.records;

import com.areeba.cms.enums.Status;

import java.util.Date;

public record CardUpdateRecord(Status status, Date expiry) {
}
