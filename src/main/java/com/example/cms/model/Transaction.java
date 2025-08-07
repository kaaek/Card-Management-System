package com.example.cms.model;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.UUID;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(name = "amount", updatable = false, nullable = false)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "date", updatable = false, nullable = false)
    private Timestamp date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private TransactionType type;

    @Column(name = "currency", nullable = false)
    private Currency currency

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "cardId")
    private Card card;

    public Transaction(BigDecimal transactionAmount, Timestamp transactionDate, TransactionType transactionType, Currency currency, Card card){
        this.amount = transactionAmount;
        this.date = transactionDate;
        this.type = transactionType;
        this.currency = currency;
        this.card = card;
    }

}
