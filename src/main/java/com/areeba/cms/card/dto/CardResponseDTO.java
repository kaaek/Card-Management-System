package com.areeba.cms.card.dto;

import com.areeba.cms.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {
    private UUID id;
    private Status status;
    private Date expiry;
    private String cardNumber;
}
