package com.areeba.cms.credit.dto;
import java.math.BigDecimal;

import com.areeba.cms.enums.Currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequestDTO {
    private String cardNumber;
    private BigDecimal amount;
    private Currency currency;
}
