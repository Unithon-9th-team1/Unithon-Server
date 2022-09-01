package com.example.springbootboilerplate.jwt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException {
        // 유저 정보는 없으나 접근할 수 없는 권한으로 접근하려 할 때 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
