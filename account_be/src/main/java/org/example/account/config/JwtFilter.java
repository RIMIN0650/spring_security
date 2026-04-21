package org.example.account.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.account.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.startsWith("/login") ||
                path.startsWith("/user/signup");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ATOKEN")) {
                    // JwtUtil에서 토큰 생성 및 확인하도록 리팩토링
                    Long userIdx = JwtUtil.getUserIdx(cookie.getValue());
                    System.out.println(userIdx);

                    String username = JwtUtil.getUsername(cookie.getValue());
                    String role = JwtUtil.getRole(cookie.getValue());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            // 토큰에 idx, pw, 권한을 설정해줘야 함
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        // 기존의 필터도 실행시키면서 내 필터도 실행시켜야 되므로
        // 내 필터가 실행이 될 때 다른 필터들도 실행이 될 수 있도록 설정
        filterChain.doFilter(request, response);
    }

}
