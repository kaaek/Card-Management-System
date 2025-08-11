package com.example.cms.dto.card;

import com.example.cms.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CardResponseDTO {
    private UUID id;
    private Status status;
    private Date expiry;
    private String cardNumber;
    private Set<UUID> accountIds;

    public CardResponseDTO(UUID id, Status status, Date expiry, String cardNumber, Set<UUID> accountIds){
        this.id = id;
        this.status = status;
        this.expiry = expiry;
        this.cardNumber = cardNumber;
        this. accountIds = accountIds;
    }
}
