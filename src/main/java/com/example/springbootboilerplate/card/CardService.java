package com.example.springbootboilerplate.card;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Long saveCard(CardRequest cardRequest){
        Card card = cardRequest.toEntity();
        Card savedCard = cardRepository.save(card);
        return savedCard.getId();
    }
}
