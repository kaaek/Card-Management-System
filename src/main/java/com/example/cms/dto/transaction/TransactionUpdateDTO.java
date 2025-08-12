package com.example.cms.dto.transaction;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateDTO {
    private BigDecimal amount;
    private TransactionType type;
    private Currency currency;
    private Timestamp date;
}
