package com.ll.sideproject.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (REST API에서는 일반적으로 비활성화)
                .csrf(csrf -> csrf.disable())

                // CORS 비활성화 (필요한 경우 설정 가능)
                .cors(cors -> cors.disable())

                // 세션 비활성화 (JWT 기반 인증을 사용하기 위해)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 경로에 따른 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/public/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/login",
                                "/register",
                                "/error" // 추가: Spring Boot 기본 에러 페이지 접근 허용
                        ).permitAll() // Swagger, 로그인 관련 경로 허용
                        .anyRequest().authenticated() // 나머지 경로는 인증 필요
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
