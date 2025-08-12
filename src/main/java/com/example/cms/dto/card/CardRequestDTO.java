package com.example.cms.dto.card;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CardRequestDTO {
    private List<UUID> accountIds;
}
