package com.example.cms.dto.card;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CardUpdateDTO {
    private String status;
    private Date expiry;
}
