package com.example.cms.dto.card;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CardUpdateDTO {
    private String status;
    private Date expiry;
    private Set<UUID> accountIds;
}
