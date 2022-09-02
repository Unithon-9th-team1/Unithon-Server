package com.example.springbootboilerplate.config;

import com.example.springbootboilerplate.jwt.JwtAccessDeniedHandler;
import com.example.springbootboilerplate.jwt.JwtAuthenticationEntryPoint;
import com.example.springbootboilerplate.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
            // exception handling할 때 우리가 만든 클래스 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            // h2-console을 위한 추가 설정
            .and()
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
            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()

            // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스 적용
            .and()
            .apply(new JwtSecurityConfig(tokenProvider));
    }
}
