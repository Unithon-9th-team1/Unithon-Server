package com.example.springbootboilerplate.auth;

import com.example.springbootboilerplate.base.dto.DataResponseDto;
import com.example.springbootboilerplate.jwt.dto.TokenDto;
import com.example.springbootboilerplate.jwt.dto.TokenRequestDto;
import com.example.springbootboilerplate.member.dto.MemberRequestDto;
import com.example.springbootboilerplate.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/sign-up")
    public DataResponseDto<Object> signUp(@RequestBody MemberRequestDto memberRequestDto) {
        return DataResponseDto.of(authService.signUp(memberRequestDto));
    }

    // 로그인
    @PostMapping("/log-in")
    public DataResponseDto<Object> logIn(@RequestBody MemberRequestDto memberRequestDto) {
        return DataResponseDto.of(authService.logIn(memberRequestDto));
    }

    // 재발급
    @PostMapping("/reissue")
    public DataResponseDto<Object> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return DataResponseDto.of(authService.reissue(tokenRequestDto));
    }
}
