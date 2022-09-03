package com.example.springbootboilerplate.rocket;

import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.MemberService;
import com.example.springbootboilerplate.rocket.dto.RocketBoardRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketBookingRequestDto;
import com.example.springbootboilerplate.rocket.dto.RocketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class RocketController {
    private final RocketService rocketService;

    // 로켓 만들기
    @PostMapping("/rocket")
    public DataResponseDto<RocketResponseDto> createRocket(@RequestBody RocketBookingRequestDto rocketRequest) {
        return DataResponseDto.of(Code.CREATED, rocketService.createRocket(rocketRequest));
    }

    // 로켓 출발하기 버튼 누른 경우 arrivalEnd 부터 +72시간 후에 updateRead 되도록
    // 로켓 출발 상태 변경 -> 탑승완료 버튼 누른 경우
    @PatchMapping("/rocket/{rocketId}")
    public DataResponseDto<Object> departRocket(@PathVariable("rocketId") Long rocketId){
        rocketService.departRocket(rocketId);
        rocketService.countDown(rocketId);
        return DataResponseDto.ofMessage(Code.OK, "탑승 상태 변경 완료");
    }

    // 프론트에서 uuid 가져오는 코드
//    @PatchMapping("/uuid")
//    public void getUuid(){
//
//    }

    // 로켓 탑승하기
    @PostMapping("/rocket-boarding")
    public DataResponseDto<RocketResponseDto> boardRocket(@RequestBody RocketBoardRequestDto rocketBoardRequest){
        rocketService.boardRocket(rocketBoardRequest);
        return DataResponseDto.of(Code.CREATED, rocketService.boardRocket(rocketBoardRequest));
    }

    /**
     * 유저 로켓 목록 조회 API - 정연
     * [GET] /rocket?nickname=(유저닉네임)
     */
    // uuid 로 바궈야하는것인지
    @GetMapping("/rockets")
    public DataResponseDto<Object> getMyRockets(@RequestParam(required = false) String nickname) {
        return DataResponseDto.of(Code.OK, rocketService.getMyRockets(nickname));
    }
}
