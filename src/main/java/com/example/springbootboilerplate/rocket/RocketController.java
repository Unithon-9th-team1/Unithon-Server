package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.rocket.dto.RocketBookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;

    // 로켓 예약하기
    @PostMapping("/book-rocket")
    public ResponseEntity<RocketBookRequestDto> bookRocket(@RequestBody RocketBookRequestDto rocketRequest){
        rocketService.bookRocket(rocketRequest);
        return ResponseEntity.ok().body(rocketRequest);
    }

    // 로켓 탑승하기
    @PostMapping("/board-rocket")
    public ResponseEntity<RocketBookRequestDto> boardRocket(@RequestBody RocketBookRequestDto rocketRequest){
        return ResponseEntity.status(404).body(rocketRequest);
    }
}
