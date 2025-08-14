package com.example.cms.repository;

import java.util.UUID;

import com.example.cms.model.Card;
import com.example.cms.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    void deleteByCard(Card card);
}
