package com.ll.sideproject.domain.member.service;

import com.ll.sideproject.domain.member.dto.MemberRole;
import com.ll.sideproject.domain.member.dto.Status;
import com.ll.sideproject.domain.member.entity.Member;
import com.ll.sideproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 (이메일 인증 포함)
    @Transactional
    public Member registerMember(String username, String email, String password) {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(MemberRole.USER)
                .status(Status.INACTIVE) // 이메일 인증 전까지 비활성 상태
                .build();

        member.encodePassword(passwordEncoder);
        member.generateEmailVerificationToken();
        return memberRepository.save(member);
    }

    // 이메일 인증 확인
    @Transactional
    public boolean verifyEmail(String token) {
        Optional<Member> memberOptional = memberRepository.findByEmailVerificationToken(token);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.verifyEmail();
            member.setStatus(Status.ACTIVE);
            memberRepository.save(member);
            return true;
        }
        return false;
    }
}