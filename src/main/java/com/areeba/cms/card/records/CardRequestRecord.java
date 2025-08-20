package com.areeba.cms.card.records;

import java.util.List;
import java.util.UUID;

public record CardRequestRecord(List<UUID> accountIds) {
}
