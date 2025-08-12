package com.example.cms.service;

import com.example.cms.dto.card.CardRequestDTO;
import com.example.cms.dto.card.CardResponseDTO;
import com.example.cms.dto.card.CardUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.AccountCard;
import com.example.cms.model.Card;
import com.example.cms.model.enums.Status;
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

    private final ModelMapper mapper;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, ModelMapper mapper) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.mapper = mapper;
    }

    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO) {

        // Fetch accounts
        List<Account> accounts = accountRepository.findAllById(cardRequestDTO.getAccountIds());
        if (accounts.size() != cardRequestDTO.getAccountIds().size()) {
            throw new RuntimeException("One or more Account IDs are invalid");
        }

        // Create card
        Card newCard = new Card(Status.ACTIVE, newExpiryDate(), generate16DigitNumber());

        // Link to accounts via join table
        for (Account account : accounts) {
            AccountCard link = new AccountCard(account, newCard);

//            newCard.getAccountCards().add(link);
//            account.getAccountCards().add(link);
//            accountCardRepository.save(link);

            newCard.getAccountCards().add(link);
            account.getAccountCards().add(link);
        }
        cardRepository.save(newCard);
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
        // Prepare parameters for response dto
        Status status = parseStatus(cardUpdateDTO.getStatus());
        Date expiry = cardUpdateDTO.getExpiry();

        // Extract card if exists
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));

        card.setStatus(status);
        card.setExpiry(expiry);

        cardRepository.save(card);

        return mapper.map(card, CardResponseDTO.class);
    }

    public void deleteCard(UUID id){
        cardRepository.deleteById(id);
    }

    // Helper methods

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

    public Status parseStatus (String s){
        try{
            return Status.valueOf(s.strip().toUpperCase());
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid status. Supported values are: ACTIVE, INACTIVE");
        }
    }
}