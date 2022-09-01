package com.example.springbootboilerplate.auth;

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
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signUp(memberRequestDto));
    }

    // 로그인
    @PostMapping("/log-in")
    public ResponseEntity<TokenDto> logIn(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.logIn(memberRequestDto));
    }

    // 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
