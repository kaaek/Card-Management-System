package com.areeba.cms.accountCard.repository;

import com.areeba.cms.account.model.Account;
import com.areeba.cms.accountCard.model.AccountCard;
import com.areeba.cms.card.model.Card;
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
