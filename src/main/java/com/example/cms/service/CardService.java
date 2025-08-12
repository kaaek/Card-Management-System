package com.example.cms.service;

import com.example.cms.dto.card.CardRequestDTO;
import com.example.cms.dto.card.CardResponseDTO;
import com.example.cms.dto.card.CardUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.AccountCard;
import com.example.cms.model.Card;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountCardRepository;
import com.example.cms.repository.AccountRepository;
import com.example.cms.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final AccountCardRepository accountCardRepository;

    private final ModelMapper mapper;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, AccountCardRepository accountCardRepository, ModelMapper mapper) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.mapper = mapper;
    }

    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO) {

        // Fetch accounts
        List<Account> accounts = accountRepository.findAllById(cardRequestDTO.getAccountIds());
        if (accounts.size() != cardRequestDTO.getAccountIds().size()) {
            throw new EntityNotFoundException("One or more Account IDs are invalid");
        }

        // Create card
        Card newCard = new Card(Status.ACTIVE, newExpiryDate(), generate16DigitNumber());

        cardRepository.save(newCard);

        // Link to accounts via join table
        for (Account account : accounts) {
            System.out.println("hello");
            AccountCard link = new AccountCard(account, newCard);
            accountCardRepository.save(link);
        }
        return mapper.map(newCard, CardResponseDTO.class);
    }

    public CardResponseDTO getCardById(UUID id){

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));

        return mapper.map(card, CardResponseDTO.class);
    }

    public List<CardResponseDTO> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> new CardResponseDTO(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber()))
                .collect(Collectors.toList());
    }

    public CardResponseDTO update(UUID id, CardUpdateDTO cardUpdateDTO){

        // Extract card if exists
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));

        card.setStatus(cardUpdateDTO.getStatus());
        card.setExpiry(cardUpdateDTO.getExpiry());

        cardRepository.save(card);

        return mapper.map(card, CardResponseDTO.class);
    }

//    public void deleteCard(UUID id){
//        Card card = cardRepository.findById(id)
//                        .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));
//        accountCardRepository.deleteByCard(card);
//        cardRepository.delete(card);
//    }

    public void deleteCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + id));

        List<AccountCard> accountCards = accountCardRepository.findByCard(card);
        accountCardRepository.deleteAll(accountCards);

        cardRepository.delete(card);
    }


    public String generate16DigitNumber() {
        Random random = new Random();
        String cardNumber;

        do {
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                int digit = random.nextInt(10); // 0-9
                sb.append(digit);
            }
            cardNumber = sb.toString(); // TO-DO: check if this number is an existing cardNumber in the db (via card repo)
        } while (cardRepository.existsByCardNumber(cardNumber));

        return cardNumber;
    }

    public Date newExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 3);
        return cal.getTime();
    }
}