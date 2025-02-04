package com.ll.sideproject.domain.member.controller;

import com.ll.sideproject.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 이메일 인증 처리 API
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        boolean verified = memberService.verifyEmail(token);
        return verified ? "이메일 인증 성공!" : "이메일 인증 실패!";
    }
}