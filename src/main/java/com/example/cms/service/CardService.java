package com.example.cms.service;

import com.example.cms.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    public final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
}