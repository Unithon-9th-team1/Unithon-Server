package com.example.springbootboilerplate.card;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/card")
    public ResponseEntity<String> saveCard(@RequestBody CardRequest cardRequest){
        cardService.saveCard(cardRequest);
        return ResponseEntity.ok().body("카드 등록 성공");
    }

}
