package com.example.springbootboilerplate.member;

import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public DataResponseDto<Object> getMyMemberInfo() {
        return DataResponseDto.of(memberService.getMyInfo());
    }

    @GetMapping("/{nickname}")
    public DataResponseDto<Object> getMemberInfo(@PathVariable String nickname) {
        return DataResponseDto.of(memberService.getMemberInfo(nickname));
    }

    @GetMapping("")
    public void checkNickname(@RequestParam("nickname") String nickname){
        memberService.checkNickname(nickname);
    }
}
