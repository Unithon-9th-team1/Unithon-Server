package com.example.springbootboilerplate.auth;

import com.example.springbootboilerplate.jwt.RefreshTokenRepository;
import com.example.springbootboilerplate.jwt.TokenProvider;
import com.example.springbootboilerplate.jwt.domain.RefreshToken;
import com.example.springbootboilerplate.jwt.dto.TokenDto;
import com.example.springbootboilerplate.jwt.dto.TokenRequestDto;
import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.member.dto.MemberRequestDto;
import com.example.springbootboilerplate.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입 - 유저 정보를 받아서 저장
    @Transactional
    public MemberResponseDto signUp(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    // 로그인
    @Transactional
    public TokenDto logIn(MemberRequestDto memberRequestDto) {
        // 1. 로그인 ID/PW 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보 기반 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    // 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 7. 토큰 발급
        return tokenDto;
    }
}
