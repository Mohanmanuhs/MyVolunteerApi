package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.dto.UserLoginRequest;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepository;


    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token, Model model) {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            model.addAttribute("error", "Invalid verification token.");
            model.addAttribute("userRequest", new UserLoginRequest());
            return "loginPage";
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        model.addAttribute("message", "Email verified successfully. You can now log in.");
        model.addAttribute("userRequest", new UserLoginRequest());
        return "loginPage";
    }
}
