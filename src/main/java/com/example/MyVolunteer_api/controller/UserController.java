package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Gender;
import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.ChangePassDto;
import com.example.MyVolunteer_api.dto.UserLoginRequest;
import com.example.MyVolunteer_api.dto.UserRegisterRequest;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.JwtService;
import com.example.MyVolunteer_api.service.user.OrganizationService;
import com.example.MyVolunteer_api.service.user.UserService;
import com.example.MyVolunteer_api.service.user.VolunteerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRequest") UserRegisterRequest userRequest,HttpServletResponse response,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userRequest", new UserRegisterRequest());
            model.addAttribute("roles", Role.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("error", "Please correct the errors in the form.");
            return "registerPage";
        }

        if (userService.findByEmail(userRequest.getEmail()) != null) {
            model.addAttribute("userRequest", new UserRegisterRequest());
            model.addAttribute("roles", Role.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("error", "User already exists with this email.");
            return "registerPage";
        }


        try {
            User user = null;

            switch (userRequest.getRole()) {
                case ORGANIZATION -> {
                    Organization organization = getOrganization(userRequest);
                    user = organizationService.createOrganization(organization);
                }
                case VOLUNTEER -> {
                    Volunteer volunteer = getVolunteer(userRequest);
                    user = volunteerService.createVolunteer(volunteer);
                }
            }
            userService.createUser(user);
            return login(new UserLoginRequest(userRequest.getEmail(),userRequest.getPassword()),response,model);
        } catch (Exception e) {
            model.addAttribute("userRequest", new UserRegisterRequest());
            model.addAttribute("roles", Role.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("error", "An error occurred during registration.");
            return "registerPage";
        }
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("userRequest") UserLoginRequest userRequest,
                        HttpServletResponse response,
                        Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userRequest.getEmail());
                Cookie cookie = new Cookie("jwt", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true); // Enable for HTTPS
                cookie.setPath("/");
                cookie.setMaxAge(7 * 24 * 60 * 60); // 1 week expiry
                response.addCookie(cookie);
                return "redirect:/test/home";
            }
        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Invalid email or password");
            return "/test/login";
        }
        model.addAttribute("error", "Login failed");
        return "/test/login";
    }

    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@Valid @RequestBody ChangePassDto changePassDto) {
        return ResponseEntity.ok(userService.changePassword(changePassDto));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        String email = userDetails.getUsername();

        userService.deleteUserByEmail(email);
        return ResponseEntity.ok("user deleted");
    }


    private static Volunteer getVolunteer(UserRegisterRequest userRequest) {
        Volunteer volunteer = new Volunteer();
        volunteer.setEmail(userRequest.getEmail());
        volunteer.setName(userRequest.getName());
        volunteer.setPassword(userRequest.getPassword());
        volunteer.setPhone(userRequest.getPhone());
        volunteer.setGender(userRequest.getGender());
        volunteer.setRole(userRequest.getRole());
        volunteer.setSkills(userRequest.getSkills());
        return volunteer;
    }

    private static Organization getOrganization(UserRegisterRequest userRequest) {
        Organization organization = new Organization();
        organization.setEmail(userRequest.getEmail());
        organization.setName(userRequest.getName());
        organization.setPassword(userRequest.getPassword());
        organization.setPhone(userRequest.getPhone());
        organization.setGender(userRequest.getGender());
        organization.setRole(userRequest.getRole());
        organization.setGstNumber(userRequest.getGstNumber());
        organization.setLocation(userRequest.getLocation());
        return organization;
    }

}