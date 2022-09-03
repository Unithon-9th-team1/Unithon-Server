package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.MemberService;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketResponseDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;
    private final MemberService memberService;

    // 로켓 예약하기
    @PostMapping("/rocket-booking")
    public DataResponseDto<RocketResponseDto> bookRocket(@RequestBody RocketBookingRequestDto rocketRequest) {
        return DataResponseDto.of(Code.CREATED, rocketService.bookRocket(rocketRequest));
    }

    // 탑승완료버튼 누른 경우 코드 저장
    @PatchMapping("/rocket/{rocketId}")
    public DataResponseDto saveRocketCode(@PathVariable("rocketId") Long rocketId, @RequestBody HashMap<String, String> hashMap){
        String code = hashMap.get("code");
        rocketService.saveRocketCode(rocketId, code);
        return DataResponseDto.ofMessage(Code.OK, "로켓 코드가 저장되었습니다");
    }

    // 로켓 탑승하기
//    @PostMapping("/rocket-boarding")
//    public RocketResponseDto boardRocket(@RequestBody RocketBoardRequestDto rocketBoardRequest){
//        String code = rocketBoardRequest.getCode();
//        String nickname = rocketBoardRequest.getNickname();
//        if(rocketService.confirmCode(code))
//            throw new GeneralException(Code.CONFLICT, "없는 코드번호입니다");
//
//        if(memberService.confirmNickname(nickname))
//            throw new GeneralException(Code.CONFLICT, "없는 닉네임입니다");
////        return rocketService.boardRocket(rocketBoardRequest);
//    }
}
