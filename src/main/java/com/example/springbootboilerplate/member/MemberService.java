package com.example.springbootboilerplate.member;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean confirmNickname(String nickname){
        return memberRepository.findByNickname(nickname).isPresent();
    }
}
