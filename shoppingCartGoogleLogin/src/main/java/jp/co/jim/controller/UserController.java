package jp.co.jim.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        model.addAttribute("email", email);
        model.addAttribute("name", name);
        return "profile";
    }
}
