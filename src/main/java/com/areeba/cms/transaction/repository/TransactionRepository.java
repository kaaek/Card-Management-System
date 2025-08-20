package com.areeba.cms.transaction.repository;

import java.util.UUID;

import com.areeba.cms.card.model.Card;
import com.areeba.cms.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    void deleteByCard(Card card);
}
