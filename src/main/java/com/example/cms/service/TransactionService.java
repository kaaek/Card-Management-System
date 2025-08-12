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
import com.example.cms.repository.AccountCardRepository;
import com.example.cms.repository.AccountRepository;
import com.example.cms.repository.CardRepository;
import com.example.cms.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import com.example.cms.model.enums.Currency;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final AccountCardRepository accountCardRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper mapper;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, AccountRepository accountRepository, AccountCardRepository accountCardRepository, ModelMapper mapper){
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.mapper = mapper;
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO){
        // Fetch cards
        Card card = findCardOrThrow(transactionRequestDTO.getCardId());

        // Prepare to create a transaction object, need the parameters amount, date, type, currency, and card id.
        BigDecimal amount = transactionRequestDTO.getAmount();

        // Check if transaction amount is positive.
        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new IllegalArgumentException("Transaction amount cannot be zero or negative.");
        }

        Timestamp date = createTimestamp();
        TransactionType type = transactionRequestDTO.getType();
        Currency currency = transactionRequestDTO.getCurrency();
        Account account = getAccountFromCard(card, currency);

        // Check eligibility (if we can create this transaction or not)
        if(!(isCardValid(card) && isAccountEligible(account, type, amount))){
            throw new IllegalArgumentException("Transaction denied: invalid card or account not eligible (inactive or insufficient balance).");
        }

        // Create transaction
        Transaction transaction = new Transaction(amount, date, type, currency, card);
        // Add or subtract value from account's balance.
        updateBalance(account, transaction);
        // Persist
        transactionRepository.save(transaction);

        return mapper.map(transaction, TransactionResponseDTO.class);
    }

    public List<TransactionResponseDTO> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCurrency(), transaction.getCard().getId()))
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(UUID id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));
        return mapper.map(transaction, TransactionResponseDTO.class);
    }

    public TransactionResponseDTO update(UUID id, TransactionUpdateDTO transactionUpdateDTO){

        // Fetch Transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));

        // Old fields
        BigDecimal oldAmount = transaction.getAmount();
//        Timestamp oldDate = transaction.getDate();
        TransactionType oldType = transaction.getType();
        Currency oldCurrency = transaction.getCurrency();

        // New fields
        BigDecimal newAmount = transactionUpdateDTO.getAmount();
        Timestamp newDate = transactionUpdateDTO.getDate();
        TransactionType newType = transactionUpdateDTO.getType();
        Currency newCurrency = transactionUpdateDTO.getCurrency();

        // Checking validity: can't change transaction's currency
        if(!newCurrency.equals(oldCurrency)){
            throw new IllegalArgumentException("Transaction currency is immutable.");
        }

        Card card = transaction.getCard();
        Account account = getAccountFromCard(card, oldCurrency);

        // Negate old transaction effect
        if(oldType == TransactionType.C) { // was credit, so subtract that amount
            account.setBalance(account.getBalance().add(BigDecimal.valueOf(-1).multiply(oldAmount)));
        } else if(oldType == TransactionType.D){ // was debit, so add that amount
            account.setBalance(account.getBalance().add(oldAmount));
        }

        // Apply new transaction effect
        if(newType == TransactionType.C){
            account.setBalance(account.getBalance().add(newAmount));
        } else if(newType == TransactionType.D){
            account.setBalance(account.getBalance().subtract(newAmount));
        } else {
            throw new IllegalArgumentException("Transaction type is invalid.");
        }

        accountRepository.save(account);

        if(newDate.after(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Transaction cannot be in the future.");
        }
        transaction.setDate(newDate);
        transaction.setAmount(newAmount);
        transaction.setType(newType);
        transactionRepository.save(transaction);

        return mapper.map(transaction, TransactionResponseDTO.class);
    }


    // Helper methods
    public void deleteTransaction(UUID id){
        // Fetch Transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));
        // Old fields
        BigDecimal oldAmount = transaction.getAmount();
        TransactionType oldType = transaction.getType();
        Currency oldCurrency = transaction.getCurrency();

        Card card = transaction.getCard();
        Account account = getAccountFromCard(card, oldCurrency);

        // Negate old transaction effect
        if(oldType == TransactionType.C) { // was credit, so subtract that amount
            account.setBalance(account.getBalance().add(BigDecimal.valueOf(-1).multiply(oldAmount)));
        } else if(oldType == TransactionType.D){ // was debit, so add that amount
            account.setBalance(account.getBalance().add(oldAmount));
        }
        transactionRepository.delete(transaction);
        accountRepository.save(account);
    }

    public Account getAccountFromCard(Card card, Currency currency) {
        // Find the card's account w/ matching currency to check:
        Optional<Account> matchingAccount = accountCardRepository.findByCard(card)
                .stream()
                .map(AccountCard::getAccount)
                .filter(acc -> acc.getCurrency().equals(currency))
                .findFirst();
        if (matchingAccount.isEmpty()) {
            throw new RuntimeException("No account with matching currency found.");
        }
        return matchingAccount.get();
    }

    public void updateBalance(Account account, Transaction transaction){
        // Assuming the card & account check out (since otherwise a transaction object would not have been instantiated)
        if (transaction.getType().equals(TransactionType.C)){ // Add money to account
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        } else if (transaction.getType().equals(TransactionType.D)) { // Take money from account
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            throw new RuntimeException("Transaction type is invalid. Could not update account's balance.");
        }
        accountRepository.save(account);
    }

    public boolean isCardValid(Card card){
        Date now = new Date();
        return card.getStatus() == Status.ACTIVE && (!card.getExpiry().before(now));
    }

    public boolean isAccountEligible(Account account, TransactionType type, BigDecimal amount) {
        if (!account.getStatus().equals(Status.ACTIVE)) {
            return false;
        }

        if (type.equals(TransactionType.D)) {
            return account.getBalance().compareTo(amount) >= 0;
        }

        return type.equals(TransactionType.C);
    }

    public Timestamp createTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public Card findCardOrThrow(UUID id) {
        return cardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Card not found"));
    }

}
