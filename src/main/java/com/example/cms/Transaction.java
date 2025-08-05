package com.example.cms;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.security.Timestamp;


@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "amount", updatable = false, nullable = false)
    private BigDecimal transactionAmount;

    @CreationTimestamp
    @Column(name = "date", updatable = false, nullable = false)
    private Timestamp transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private transactionType transactionType;
}
