package com.areeba.cms.account.dto;

import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private UUID id;
    private Status status;
    private BigDecimal balance;
    private Currency currency;
}
