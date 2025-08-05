package com.example.cms;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.UUID;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", updatable = true, nullable = false)
    private CardStatus status;

    @Column (name = "date", updatable = true, nullable = false)
    private Date expiry;

    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", updatable = false, nullable = false, unique = true)
    private String cardNumber;

    @ManyToMany(mappedBy = "cards")
    private Set<Account> accounts;

}
