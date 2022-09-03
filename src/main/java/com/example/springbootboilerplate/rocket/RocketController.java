package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.MemberService;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;
    private final MemberService memberService;

    // 로켓 만들기
    @PostMapping("/rocket")
    public DataResponseDto<RocketResponseDto> createRocket(@RequestBody RocketBookingRequestDto rocketRequest) {
        return DataResponseDto.of(Code.CREATED, rocketService.createRocket(rocketRequest));
    }

    // 로켓 출발 상태 변경 -> 탑승완료 버튼 누른 경우
    @PatchMapping("/rocket/{rocketId}")
    public DataResponseDto<Object> departRocket(@PathVariable("rocketId") Long rocketId){
        rocketService.departRocket(rocketId);
        return DataResponseDto.ofMessage(Code.OK, "탑승 상태 변경 완료");
    }
    // 프론트에서 uuid 가져오는 코드
//    @PatchMapping("/uuid")
//    public void getUuid(){
//
//    }

//    // 탑승완료버튼 누른 경우 코드 저장
//    @PatchMapping("/rocket/{rocketId}")
//    public DataResponseDto saveRocketCode(@PathVariable("rocketId") Long rocketId, @RequestBody HashMap<String, String> hashMap){
//        String code = hashMap.get("code");
//        rocketService.saveRocketCode(rocketId, code);
//        return DataResponseDto.ofMessage(Code.OK, "로켓 코드가 저장되었습니다");
//    }

    // 로켓 탑승하기
    @PostMapping("/rocket-boarding")
    public DataResponseDto<RocketResponseDto> boardRocket(@RequestBody RocketBoardRequestDto rocketBoardRequest){
        rocketService.boardRocket(rocketBoardRequest);
        return DataResponseDto.of(Code.CREATED, rocketService.boardRocket(rocketBoardRequest));
    }
}
