package com.example.springbootboilerplate.member;

import com.example.springbootboilerplate.base.GeneralException;
import com.example.springbootboilerplate.base.constant.Code;
import com.example.springbootboilerplate.jwt.util.SecurityUtil;
import com.example.springbootboilerplate.member.domain.Member;
import com.example.springbootboilerplate.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new GeneralException(Code.USER_NOT_FOUND, "유저 정보가 없습니다."));
    }

    // 현재 SecurityContext에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new GeneralException(Code.USER_NOT_FOUND, "유저 정보가 없습니다."));
    }

    public Member getMember(long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new GeneralException(Code.BAD_REQUEST, "Exception 테스트"));
    }

}
