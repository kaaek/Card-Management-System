package com.example.cms.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String type;
    private UUID cardId;

}
