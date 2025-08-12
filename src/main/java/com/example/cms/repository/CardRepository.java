package com.example.cms.repository;

import java.util.UUID;

import com.example.cms.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByCardNumber(String cardNumber);

    Card findByCardNumber(String cardNumber);

}
