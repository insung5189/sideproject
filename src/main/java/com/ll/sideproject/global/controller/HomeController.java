package com.ll.sideproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        String welcomeMsg = "Welcome to the Sample Homepage!";
        model.addAttribute("message", welcomeMsg);
        return "home"; // home.html 파일을 렌더링
    }
}
