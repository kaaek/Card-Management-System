package com.example.cms.dto;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountResponseDTO {
    private UUID id;
    private Status status;
    private BigDecimal balance;
    private Currency currency;
    // Add cards?

    public AccountResponseDTO(UUID id, Status status, BigDecimal balance, Currency currency){
        this.id = id;
        this.status = status;
        this.balance = balance;
        this.currency = currency;
    }

}
