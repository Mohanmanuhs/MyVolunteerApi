package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Gender;
import com.example.MyVolunteer_api.constants.OpportunityStatus;
import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.UserLoginRequest;
import com.example.MyVolunteer_api.dto.UserRegisterRequest;
import com.example.MyVolunteer_api.dto.VolOppSaveDto;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String email = null;
        if (principal instanceof UserPrincipal) {
            UserPrincipal userDetails = (UserPrincipal) principal;
            email = userDetails.getUsername();
        } else if (principal instanceof String) {
            email = (String) principal; // For cases like anonymous or basic auth
        }

        User user = (email != null) ? userService.findByEmail(email) : null;
        model.addAttribute("role", (user == null) ? "user" : user.getRole().toString());
        return "index";
    }

    @GetMapping("/login")
    public String loginRequestPage(Model model) {
        model.addAttribute("userRequest", new UserLoginRequest());
        return "loginPage";
    }

    @GetMapping("/register")
    public String registerRequestPage(Model model) {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        model.addAttribute("userRequest", userRegisterRequest);
        model.addAttribute("roles", Role.values());
        model.addAttribute("genders", Gender.values());
        return "registerPage";
    }

    @GetMapping("/create")
    public String createVolOpp(Model model) {
        VolOppSaveDto volOpp = new VolOppSaveDto();
        model.addAttribute("statuses", OpportunityStatus.values());
        model.addAttribute("volOpp",volOpp);
        return "createVolOpp";
    }



}
