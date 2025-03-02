package com.ll.sideproject.domain.member.entity;

import com.ll.sideproject.base.entity.BaseEntity;
import com.ll.sideproject.domain.member.dto.MemberRole;
import com.ll.sideproject.domain.member.dto.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호 (소셜 로그인은 빈 값일 수 있음)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role; // 사용자 권한

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status; // 계정 상태 (활성, 비활성, 정지 등)

    @Column(nullable = true, length = 50)
    private String provider; // 소셜 로그인 제공자 (GOOGLE, KAKAO, NAVER 등)

    @Column(nullable = true, unique = true, length = 100)
    private String providerId; // 소셜 로그인 제공자의 고유 ID

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부

    @Column(length = 100)
    private String emailVerificationToken; // 이메일 인증 토큰

    private LocalDateTime emailVerificationExpiresAt; // 이메일 인증 유효 기간

    // 🔹 비밀번호 암호화 설정
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 🔹 이메일 인증 완료 처리
    public void verifyEmail() {
        this.emailVerified = true;
        this.emailVerificationToken = null;
        this.emailVerificationExpiresAt = null;
    }

    // 🔹 이메일 인증 토큰 생성
    public void generateEmailVerificationToken() {
        this.emailVerificationToken = java.util.UUID.randomUUID().toString();
        this.emailVerificationExpiresAt = LocalDateTime.now().plusHours(1); // 1시간 유효
    }

    // 🔹 소셜 로그인 회원 생성 메서드
    public static Member createSocialMember(String provider, String providerId, String email, String username) {
        return Member.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)
                .username(username)
                .password("") // 소셜 로그인은 비밀번호 없음
                .emailVerified(true) // 소셜 로그인은 이메일 인증이 필요 없음
                .role(MemberRole.USER)
                .status(Status.ACTIVE)
                .build();
    }
}
