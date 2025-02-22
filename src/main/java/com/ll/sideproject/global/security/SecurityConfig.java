package com.ll.sideproject.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 인코더 (BCrypt 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 기본 사용자 등록 (In-Memory)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("password")) // "password"를 BCrypt로 인코딩
                .roles("ADMIN")
                .build());
        manager.createUser(User.withUsername("user")
                .password(passwordEncoder.encode("password")) // "password"를 BCrypt로 인코딩
                .roles("USER")
                .build());
        return manager;
    }

    // Security Filter Chain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF는 기본적으로 활성화됨
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/login", "/register").permitAll() // 비인증 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/") // 로그인 성공 시 이동 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
