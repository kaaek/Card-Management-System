package com.areeba.cms.card.model;

import com.areeba.cms.converter.CryptoConverter;
import com.areeba.cms.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false)
    private Status status;

    @Column (name = "date", nullable = false)
    private Date expiry;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", updatable = false, nullable = false, unique = true)
    private String cardNumber;

    public Card(Status status, Date expiry, String cardNumber){
        this.status = status;
        this.expiry = expiry;
        this.cardNumber = cardNumber;
    }
}
