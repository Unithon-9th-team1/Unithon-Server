package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.rocket.dto.RocketBookRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;

    // 로켓 예약하기
    @PostMapping("/rocket-booking")
    public ResponseEntity<RocketBookingResponseDto> bookRocket(@RequestBody RocketBookRequestDto rocketRequest){
        return ResponseEntity.ok().body(rocketService.bookRocket(rocketRequest));
    }

    // 특정 로켓 대기자 확인
//    @GetMapping("/rocket/{rocketID}")
//    public ResponseEntity<RocketBookRequestDto> bookRocket(@RequestBody RocketBookRequestDto rocketRequest){
//        rocketService.bookRocket(rocketRequest);
//        return ResponseEntity.ok().body(rocketRequest);
//    }

    // 로켓 탑승하기
    @PostMapping("/rocket-boarding")
    public ResponseEntity<RocketBookRequestDto> boardRocket(@RequestBody RocketBookRequestDto rocketRequest){
        return ResponseEntity.status(404).body(rocketRequest);
    }


}
