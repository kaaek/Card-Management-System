package com.example.cms.dto.account;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountUpdateDTO {
    private Status status;
    private BigDecimal balance;
}
