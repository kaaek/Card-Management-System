package com.example.cms.repository;

import com.example.cms.model.Account;
import com.example.cms.model.AccountCard;
import com.example.cms.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountCardRepository extends JpaRepository<AccountCard, UUID> {

    List<AccountCard> findByAccount(Account account);

    List<AccountCard> findByCard(Card card);

    boolean existsByCard(Card card);

    void deleteByCard(Card card);

    void deleteByAccount(Account account);
}
