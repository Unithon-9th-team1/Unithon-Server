package com.example.springbootboilerplate.card;

import lombok.Getter;
@Getter
public class CardRequest {
    private String championName;


    public Card toEntity(){
        return Card.builder()
                .championName(championName)
                .build();
    }
}
