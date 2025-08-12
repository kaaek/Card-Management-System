package com.example.cms.dto.card;
import com.example.cms.model.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CardUpdateDTO {
    private Status status;
    private Date expiry;
}
