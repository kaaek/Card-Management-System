package com.example.cms.dto.account;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;

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
