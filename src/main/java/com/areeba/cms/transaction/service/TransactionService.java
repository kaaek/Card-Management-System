package com.areeba.cms.transaction.service;
import com.areeba.cms.credit.record.CreditRequestRecord;
import com.areeba.cms.debit.record.DebitRequestRecord;
import com.areeba.cms.account.model.Account;
import com.areeba.cms.accountCard.model.AccountCard;
import com.areeba.cms.card.model.Card;
import com.areeba.cms.transaction.model.Transaction;
import com.areeba.cms.enums.Status;
import com.areeba.cms.enums.TransactionType;
import com.areeba.cms.accountCard.repository.AccountCardRepository;
import com.areeba.cms.account.repository.AccountRepository;
import com.areeba.cms.card.repository.CardRepository;
import com.areeba.cms.transaction.record.TransactionRequestRecord;
import com.areeba.cms.transaction.record.TransactionResponseRecord;
import com.areeba.cms.transaction.record.TransactionUpdateRecord;
import com.areeba.cms.transaction.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import com.areeba.cms.enums.Currency;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final AccountCardRepository accountCardRepository;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper mapper;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, AccountRepository accountRepository, AccountCardRepository accountCardRepository, ObjectMapper mapper){
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.mapper = mapper;
    }

    public TransactionResponseRecord createTransaction(TransactionRequestRecord request){
        // Fetch cards
        String cardNumber = request.cardNumber();
        Card card = cardRepository.findByCardNumber(cardNumber)
            .orElseThrow(() -> new EntityNotFoundException("Card with number: " + cardNumber + " was not found."));

        // Prepare to create a transaction object, need the parameters amount, date, type, currency, and card id.
        BigDecimal amount = request.amount();

        // Check if transaction amount is positive.
        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new IllegalArgumentException("Transaction amount cannot be zero or negative.");
        }

        Timestamp date = createTimestamp();
        TransactionType type = request.type();
        Currency currency = request.currency();

        // Check eligibility (if we can create this transaction or not)
        Account account = getAccountFromCard(card, currency);

        if(!(isCardValid(card) && isAccountEligible(account, type, amount))){
            throw new IllegalArgumentException("Transaction denied: invalid card or account not eligible (inactive or insufficient balance).");
        }

        // Create transaction
        Transaction transaction = new Transaction(amount, date, type, currency, card);
        // Add or subtract value from account's balance.
        updateBalance(account, transaction);
        // Persist
        transactionRepository.save(transaction);

        return mapper.convertValue(transaction, TransactionResponseRecord.class);
    }

    public TransactionResponseRecord debit (DebitRequestRecord request) {
        // DTO fields:
        String cardNumber = request.cardNumber();
        BigDecimal amount = request.amount();
        Currency currency = request.currency();

        Card card = cardRepository.findByCardNumber(cardNumber)
            .orElseThrow(() -> new IllegalArgumentException("Card with number: "+cardNumber+" not found in db."));

        // Check if transaction amount is positive.
        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new IllegalArgumentException("Transaction amount cannot be zero or negative.");
        }

        Timestamp date = createTimestamp();
        TransactionType type = TransactionType.D; // Debit
        
        // Check eligibility (if we can create this transaction or not)
        Account account = getAccountFromCard(card, currency);

        if(!(isCardValid(card) && isAccountEligible(account, type, amount))){
            throw new IllegalArgumentException("Transaction denied: invalid card or account not eligible (inactive or insufficient balance).");
        }

        // Create transaction
        Transaction transaction = new Transaction(amount, date, type, currency, card);
        // Add or subtract value from account's balance.
        updateBalance(account, transaction);
        // Persist
        transactionRepository.save(transaction);

        return mapper.convertValue(transaction, TransactionResponseRecord.class);
    }

    public TransactionResponseRecord credit (CreditRequestRecord request) {
        // DTO Fields:
        String cardNumber = request.cardNumber();
        BigDecimal amount = request.amount();
        Currency currency = request.currency();

        Card card = cardRepository.findByCardNumber(cardNumber)
            .orElseThrow(() -> new IllegalArgumentException("Card with number: "+cardNumber+" not found in db."));

        // Check if transaction amount is positive.
        if(amount.compareTo(BigDecimal.valueOf(0)) <= 0){
            throw new IllegalArgumentException("Transaction amount cannot be zero or negative.");
        }

        Timestamp date = createTimestamp();
        TransactionType type = TransactionType.C; // Credit

        // Create transaction
        Transaction transaction = new Transaction(amount, date, type, currency, card);
        
        // Add or subtract value from account's balance.
        Account account = getAccountFromCard(card, currency);
        updateBalance(account, transaction);
        // Persist
        transactionRepository.save(transaction);

        return mapper.convertValue(transaction, TransactionResponseRecord.class);
    }

    public List<TransactionResponseRecord> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponseRecord(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCurrency(), transaction.getCard().getId()))
                .collect(Collectors.toList());
    }

    public TransactionResponseRecord getTransactionById(UUID id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));
        return mapper.convertValue(transaction, TransactionResponseRecord.class);
    }

    public TransactionResponseRecord update(UUID id, TransactionUpdateRecord update){

        // Fetch Transaction
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with ID: " + id));

        // Old fields
        BigDecimal oldAmount = transaction.getAmount();
//        Timestamp oldDate = transaction.getDate();
        TransactionType oldType = transaction.getType();
        Currency oldCurrency = transaction.getCurrency();

        // New fields
        BigDecimal newAmount = update.amount();
        Timestamp newDate = update.date();
        TransactionType newType = update.type();

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

        return mapper.convertValue(transaction, TransactionResponseRecord.class);
    }


    // Helper methods
    public String deleteTransaction(UUID id){
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

        return "Transaction with ID: " + id + " was deleted.";
    }

    public Account getAccountFromCard(Card card, Currency currency) {
        // Find the card's account w/ matching currency to check:
        Optional<Account> matchingAccount = accountCardRepository.findByCard(card)
                .stream()
                .map(AccountCard::getAccount)
                .filter(acc -> acc.getCurrency().equals(currency))
                .findFirst();
        if (matchingAccount.isEmpty()) {
            throw new IllegalArgumentException("No account with matching currency found.");
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
            throw new IllegalArgumentException("Transaction type is invalid. Could not update account's balance.");
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
