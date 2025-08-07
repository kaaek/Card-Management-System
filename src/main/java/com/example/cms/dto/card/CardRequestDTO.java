package com.example.cms.dto.card;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CardRequestDTO {
    private Set<UUID> accountIds;
}
