package org.example.account.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;
    private final LoginFilter loginFilter;


    @Bean // 개발자가 직접 개발한 코드가 아닌 클래스의 객체를 스프링의 빈으로 등록하려고 할 때 사용
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers("/login", "/user/signup").permitAll() // /login URL 은 모든 사용자 허용
                        .requestMatchers("/test/ex01").permitAll()
                        .requestMatchers("/test/ex02").authenticated()
                        .requestMatchers("/test/ex03").hasRole("USER")
                        .requestMatchers("/test/ex04").hasRole("ADMIN")
                        .requestMatchers("/test").authenticated() // /test URL은 로그인 한 사용자만 허용
                        .requestMatchers("/board/**").hasRole("ADMIN")
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
