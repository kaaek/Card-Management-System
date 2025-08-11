package com.example.cms.dto.card;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CardRequestDTO {
    private Set<UUID> accountIds;
}
