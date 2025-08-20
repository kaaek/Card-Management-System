package com.areeba.cms.transaction.dto;
import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private TransactionType type;
    private Currency currency;
    private String cardNumber;
}
