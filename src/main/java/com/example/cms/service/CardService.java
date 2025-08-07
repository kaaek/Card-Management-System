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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final AccountCardRepository accountCardRepository;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, AccountCardRepository accountCardRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
    }

    public CardResponseDTO createCard(CardRequestDTO cardRequestDTO) {
        // Fetch accounts
        List<Account> accounts = accountRepository.findAllById(cardRequestDTO.getAccountIds());
        if (accounts.size() != cardRequestDTO.getAccountIds().size()) {
            throw new RuntimeException("One or more Account IDs are invalid");
        }
        // Create card
        Card card = new Card(Status.ACTIVE, newExpiryDate(), generate16DigitNumber());
        card = cardRepository.save(card);

        // Link to accounts via join table
        for (Account account : accounts) {
            AccountCard link = new AccountCard(account, card);

            // bidirectional persistence:
            card.getAccountCards().add(link);
            account.getAccountCards().add(link);
            accountCardRepository.save(link);
        }

        return new CardResponseDTO(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber(), mapAccountsToIds(card));
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

    public Set<UUID> mapAccountsToIds(Card card) {
        return card.getAccountCards().stream()
                .map(accountCard -> accountCard.getAccount().getId())
                .collect(Collectors.toSet());
    }


    public Set<Account> mapIdsToAccounts(Set<UUID> accountIds) {
        List<Account> accounts = accountRepository.findAllById(accountIds);
        Set<UUID> foundIds = accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toSet());
        // to check consistency:
        Set<UUID> missingIds = new HashSet<>(accountIds);
        missingIds.removeAll(foundIds);
        if(!missingIds.isEmpty()){
            throw new IllegalArgumentException("Invalid account IDs: " + missingIds);
        }
        return new HashSet<>(accounts);
    }


    public CardResponseDTO getCardById(UUID id){
        Optional<Card> optionalCard = cardRepository.findById(id);
        if(optionalCard.isPresent()){
            Card card = optionalCard.get();
            return new CardResponseDTO(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber(), mapAccountsToIds(card));
        } else {
            throw new RuntimeException("Card not found");
        }
    }

    public List<CardResponseDTO> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> new CardResponseDTO(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber(), mapAccountsToIds(card)))
                .collect(Collectors.toList());
    }

    public CardResponseDTO update(UUID id, CardUpdateDTO cardUpdateDTO){
        // My params
        Status status = cardUpdateDTO.getStatus();
        Date expiry = cardUpdateDTO.getExpiry();
        Set<UUID> accountIds = cardUpdateDTO.getAccountIds();

        // Extract card if exists
        Optional<Card> optionalCard = cardRepository.findById(id);

        if(optionalCard.isPresent()){
            Card card = optionalCard.get();
            card.setStatus(status);
            card.setExpiry(expiry);

            // Flush old associated links
            accountCardRepository.deleteAll(card.getAccountCards());

            Set<Account> accounts = mapIdsToAccounts(accountIds);
            Set<AccountCard> links = new HashSet<>();
            for (Account account : accounts) {
                AccountCard link = new AccountCard(account, card);
                links.add(accountCardRepository.save(link));
            }

            card.setAccountCards(links);
            Card savedCard = cardRepository.save(card);

            return new CardResponseDTO(
                    savedCard.getId(),
                    savedCard.getStatus(),
                    savedCard.getExpiry(),
                    savedCard.getCardNumber(),
                    mapAccountsToIds(savedCard)
            );
        } else {
            throw new RuntimeException("Card not found");
        }
    }

    // TO-DO: add per-field update method.

    public void deleteCard(UUID id){
        cardRepository.deleteById(id);
    }
}