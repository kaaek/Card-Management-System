package com.example.cms.dto.account;

import com.example.cms.model.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {
    private BigDecimal balance;
    private Currency currency;
}
