package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.dto.UserLoginRequest;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepository;


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp, Model model) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Invalid email.");
            return "verifyOtpPage";
        }

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            model.addAttribute("error", "Invalid OTP.");
            model.addAttribute("email", user.getEmail());
            return "verifyOtpPage";
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "OTP expired. Please request a new OTP.");
            model.addAttribute("email", user.getEmail());
            return "verifyOtpPage";
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        model.addAttribute("message", "OTP verified successfully. You can now log in.");
        model.addAttribute("userRequest", new UserLoginRequest());
        return "loginPage";
    }
}
