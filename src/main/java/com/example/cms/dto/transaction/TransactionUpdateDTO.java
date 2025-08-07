package com.example.cms.dto.transaction;

import com.example.cms.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateDTO {
    private BigDecimal amount;
    private String type;
    private String currency;
    private UUID cardId;
}
