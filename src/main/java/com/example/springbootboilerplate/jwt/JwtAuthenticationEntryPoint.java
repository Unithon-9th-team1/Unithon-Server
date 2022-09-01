package com.example.springbootboilerplate.jwt;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
        throws IOException {
        // 유효한 자격 증명을 제공하지 않고 접근할 시 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
