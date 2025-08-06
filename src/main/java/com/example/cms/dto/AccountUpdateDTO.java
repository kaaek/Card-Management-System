package com.example.cms.dto;

import com.example.cms.model.enums.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountUpdateDTO {
    private Status status;
    private BigDecimal balance;
}
