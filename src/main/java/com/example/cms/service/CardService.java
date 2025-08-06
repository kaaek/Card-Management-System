package com.example.cms.service;

import com.example.cms.dto.card.CardRequestDTO;
import com.example.cms.dto.card.CardResponseDTO;
import com.example.cms.model.Account;
import com.example.cms.model.Card;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountRepository;
import com.example.cms.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private final CardRepository cardRepository;

    @Autowired
    private final AccountRepository accountRepository;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO){
        Date expiry = getExpiryDate();
        String newCardNumber = generate16DigitNumber();
        Card card = new Card(Status.ACTIVE, expiry, newCardNumber);
        card.setAccounts(new HashSet<>(accountRepository.findAllById(cardRequestDTO.getAccountIds())));
        Set<UUID> accountIds = card.getAccounts().stream()
                .map(Account::getId)
                .collect(Collectors.toSet());
        return new CardResponseDTO(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber(), accountIds);
    }
    
    public String generate16DigitNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // 0-9
            sb.append(digit);
        }
        return sb.toString();
    }

    public Date getExpiryDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 3);
        return cal.getTime();
    }

}