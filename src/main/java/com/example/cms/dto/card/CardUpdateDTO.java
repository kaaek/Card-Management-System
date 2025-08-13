package com.example.cms.dto.card;
import com.example.cms.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateDTO {
    private Status status;
    private Date expiry;
}
