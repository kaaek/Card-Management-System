package com.areeba.cms.transaction.dto;
import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private UUID id;
    private BigDecimal amount;
    private Timestamp date;
    private TransactionType type;
    private Currency currency;
    private UUID cardId;

}
