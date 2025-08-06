package com.example.cms.service;

import com.example.cms.model.Card;
import com.example.cms.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    @Autowired
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /*
    public Card createCard(){
        cardRepository.save()
    }
     */
}