package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.MemberService;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;
    private final MemberService memberService;

    // 로켓 예약하기
    @PostMapping("/rocket-booking")
    public ResponseEntity<RocketBookingResponseDto> bookRocket(@RequestBody RocketBookingRequestDto rocketRequest){
        return ResponseEntity.ok().body(rocketService.bookRocket(rocketRequest));
    }

    // 탑승완료버튼 누른 경우 코드 저장
    @PatchMapping("/rocket/{rocketId}")
    public DataResponseDto saveRocketCode(@PathVariable("rocketId") Long rocketId, String code){
        rocketService.saveRocketCode(rocketId, code);
        return DataResponseDto.of(Code.OK, "로켓 코드가 저장되었습니다");
    }

    // 로켓 탑승하기
    @PostMapping("/rocket-boarding")
    public DataResponseDto<Object> boardRocket(@RequestBody RocketBoardRequestDto rocketBoardRequest){
        String code = rocketBoardRequest.getCode();
        String nickname = rocketBoardRequest.getNickname();
        if(rocketService.confirmCode(code))
            throw new GeneralException(Code.CONFLICT, "없는 코드번호입니다");

        if(memberService.confirmNickname(nickname))
            throw new GeneralException(Code.CONFLICT, "없는 닉네임입니다");
        rocketService.boardRocket(rocketBoardRequest);
    }

    // 특정 로켓 대기자 확인
//    @GetMapping("/rocket/{rocketID}")
//    public ResponseEntity<RocketBookRequestDto> bookRocket(@RequestBody RocketBookRequestDto rocketRequest){
//        rocketService.bookRocket(rocketRequest);
//        return ResponseEntity.ok().body(rocketRequest);
//    }

    // 로켓 탑승하기
    @PostMapping("/rocket-boarding")
    public ResponseEntity<RocketBookingRequestDto> boardRocket(@RequestBody RocketBookingRequestDto rocketRequest){
        return ResponseEntity.status(404).body(rocketRequest);
    }


}
