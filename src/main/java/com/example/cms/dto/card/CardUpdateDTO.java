package com.example.cms.dto.card;

import com.example.cms.model.Account;
import com.example.cms.model.enums.Status;
import lombok.Data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class CardUpdateDTO {
    private String status;
    private Date expiry;
    private Set<UUID> accountIds;
}
