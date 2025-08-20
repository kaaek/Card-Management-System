package com.areeba.cms.transaction.dto;
import com.areeba.cms.enums.TransactionType;
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
    private Timestamp date;
}
