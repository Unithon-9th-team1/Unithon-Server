package com.example.springbootboilerplate.jwt;

import com.example.springbootboilerplate.member.MemberRepository;
import com.example.springbootboilerplate.member.domain.Member;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
            .map(this::createdUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username + "-> 데이터베이스에서 찾을 수 없습니다."));
    }

    // DB에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createdUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
            String.valueOf(member.getId()),
            member.getPassword(),
            Collections.singleton(grantedAuthority)
        );
    }

}
