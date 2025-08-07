package com.example.cms.dto.transaction;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDTO {
    private BigDecimal amount;
    private String type;

}
