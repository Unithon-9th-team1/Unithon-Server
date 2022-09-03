package com.example.springbootboilerplate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // H2 Database 테스트가 원활하도록 관련 API들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 disable
        http.csrf().disable()
            // h2-console을 위한 추가 설정
            .headers()
            .frameOptions()
            .sameOrigin()

            // 시큐리티는 기본적으로 세션 사용
            // 세션을 사용하지 않도록 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 로그인, 회원가입 API는 토큰 없이 요청이 들어오므로 permitAll 설정
            .and()
            .authorizeRequests()
            .antMatchers("/**").permitAll() // 개발을 위해 모든 URI 임시 허용
//            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated();
    }
}

