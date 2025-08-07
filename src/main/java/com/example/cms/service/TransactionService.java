package com.example.cms.service;

import com.example.cms.dto.transaction.TransactionRequestDTO;
import com.example.cms.dto.transaction.TransactionResponseDTO;
import com.example.cms.model.Card;
import com.example.cms.model.Transaction;
import com.example.cms.model.enums.TransactionType;
import com.example.cms.repository.CardRepository;
import com.example.cms.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository){
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO){
        // Fetch cards
        Optional<Card> query = cardRepository.findById(transactionRequestDTO.getCardId());
        if (query.isEmpty()){
            throw new RuntimeException("Card not found.");
        }

        Card card = query.get();

        // Construct transaction object
        BigDecimal amount = transactionRequestDTO.getAmount();
        Timestamp date = createTimestamp();
        TransactionType type;
        try {
            type = TransactionType.valueOf(transactionRequestDTO.getType().strip().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid type. Supported values: C (Credit), D (Debit)");
        }
        Transaction transaction = new Transaction(amount, date, type, card);
        // Persist
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getDate(),
                savedTransaction.getType(),
                savedTransaction.getCard().getId()
        );
    }

    public Timestamp createTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    public List<TransactionResponseDTO> getAllTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCard().getId()))
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(UUID id){
        Optional<Transaction> query = transactionRepository.findById(id);
        if(query.isPresent()){
            Transaction transaction = query.get();
            return new TransactionResponseDTO(transaction.getId(), transaction.getAmount(), transaction.getDate(), transaction.getType(), transaction.getCard().getId());
        } else {
            throw new RuntimeException("Transaction not found.");
        }
    }

    public TransactionResponseDTO update(UUID id, TransactionUpdateDTO transactionUpdateDTO){

    }





}
