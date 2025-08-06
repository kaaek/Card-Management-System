package com.example.cms.dto;

import com.example.cms.model.enums.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    private Currency currency;
    private BigDecimal balance;
}
