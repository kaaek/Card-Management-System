package com.example.cms.dto.card;

import com.example.cms.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
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
