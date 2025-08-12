package com.example.cms.dto.transaction;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private TransactionType type;
    private Currency currency;
    private UUID cardId;

}
