package com.example.springbootboilerplate.member;

import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    public DataResponseDto<Object> getMyMemberInfo() {
        return DataResponseDto.of(memberService.getMyInfo());
    }

    @GetMapping("/{email}")
    public DataResponseDto<Object> getMemberInfo(@PathVariable String email) {
        return DataResponseDto.of(memberService.getMemberInfo(email));
    }

    @GetMapping("/test/{id}")
    public DataResponseDto<Object> test(@PathVariable long id) {
        return DataResponseDto.of(memberService.getMember(id));
    }
}
