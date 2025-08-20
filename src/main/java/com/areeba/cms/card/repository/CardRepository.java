package com.areeba.cms.card.repository;

import java.util.Optional;
import java.util.UUID;

import com.areeba.cms.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    boolean existsByCardNumber(String cardNumber);

    Optional<Card> findByCardNumber(String cardNumber);

}
