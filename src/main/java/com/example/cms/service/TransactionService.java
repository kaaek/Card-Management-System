package com.example.cms.service;

import com.example.cms.dto.transaction.TransactionRequestDTO;
import com.example.cms.dto.transaction.TransactionResponseDTO;
import com.example.cms.dto.transaction.TransactionUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.AccountCard;
import com.example.cms.model.Card;
import com.example.cms.model.Transaction;
import com.example.cms.model.enums.Status;
import com.example.cms.model.enums.TransactionType;
import com.example.cms.repository.CardRepository;
import com.example.cms.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import com.example.cms.model.enums.Currency;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository){
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    public Card findCardOrThrow(UUID id) {
        return cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }


    public TransactionType parseTransactionType(String s){
        try {
            return TransactionType.valueOf(s.strip().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type. Supported values: C (Credit), D (Debit)");
        }
    }

    public Currency parseCurrency(String s){
        try {
            return Currency.valueOf(s.strip().toUpperCase());
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid currency. Supported values are: USD, LBP");
        }
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO){
        // Fetch cards
        Card card = findCardOrThrow(transactionRequestDTO.getCardId());

        // Construct transaction object
        BigDecimal amount = transactionRequestDTO.getAmount();
        Timestamp date = createTimestamp();
        TransactionType type = parseTransactionType(transactionRequestDTO.getType());
        Currency currency = parseCurrency(transactionRequestDTO.getCurrency());
        // Find the card's account w/ matching currency to check:
        Optional<Account> matchingAccount = card.getAccountCards()
                .stream()
                .map(AccountCard::getAccount)
                .filter(acc -> acc.getCurrency().equals(currency))
                .findFirst();

        if (matchingAccount.isEmpty()) {
            throw new RuntimeException("No account with matching currency found.");
        }

        if(!(isCardValid(card) && isAccountEligible(matchingAccount.get(), type, amount))){
            throw new RuntimeException("Transaction denied: invalid card or account not eligible.");
        }

        Transaction transaction = new Transaction(amount, date, type, currency, card);
        card.addTransaction(transaction);
        // Persist
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getDate(),
                savedTransaction.getType(),
                savedTransaction.getCurrency(),
                savedTransaction.getCard().getId()
        );
    }

    public boolean isCardValid(Card card){
        Date now = new Date();
        return card.getStatus() == Status.ACTIVE && (card.getExpiry().equals(now) || card.getExpiry().after(now));

    }

    public boolean isAccountEligible(Account account, TransactionType type, BigDecimal amount) {
        if (!account.getStatus().equals(Status.ACTIVE)) {
            return false;
        }

        if (type.equals(TransactionType.D)) {
            return account.getBalance().compareTo(amount) >= 0;
        }

        if (type.equals(TransactionType.C)) {
            return true;
        }

        return false;
    }


    public Timestamp createTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public List<TransactionResponseDTO> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCurrency(), transaction.getCard().getId()))
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(UUID id){
        Optional<Transaction> query = transactionRepository.findById(id);
        if(query.isPresent()){
            Transaction transaction = query.get();
            return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCurrency(), transaction.getCard().getId());
        } else {
            throw new RuntimeException("Transaction not found.");
        }
    }

    public TransactionResponseDTO update(UUID id, TransactionUpdateDTO transactionUpdateDTO){
        // Extract transaction if exists
        Optional<Transaction> query = transactionRepository.findById(id);
        if(query.isPresent()){
            // Params
            BigDecimal amount = transactionUpdateDTO.getAmount();
            TransactionType type = parseTransactionType(transactionUpdateDTO.getType());
            UUID cardId = transactionUpdateDTO.getCardId();
            Transaction transaction = query.get();
            transaction.setAmount(amount);
            transaction.setType(type);
            Currency newCurrency = parseCurrency(transactionUpdateDTO.getCurrency());
            transaction.setCurrency(newCurrency);
            // Fetch card.
            Card newCard = findCardOrThrow(cardId);
            // Check if the card provided in the update DTO matches the one stored in the db.
            Card oldCard = transaction.getCard();
            if (oldCard != null && !oldCard.equals(newCard)) { // card is different.
                oldCard.removeTransaction(transaction);
                newCard.addTransaction(transaction);
            }
            transaction.setCard(newCard);
            transactionRepository.save(transaction);
            return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCurrency(), transaction.getCard().getId());
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

    // TO-DO: add per-field update method.

    public void deleteTransaction(UUID id){
        transactionRepository.deleteById(id);
    }

}
