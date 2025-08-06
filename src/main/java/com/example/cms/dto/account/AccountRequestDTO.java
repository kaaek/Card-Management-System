package com.example.cms.dto.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    private String currency;
    private BigDecimal balance;
}
