package com.example.MyVolunteer_api.controller;

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
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping("register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegisterRequest userRequest) {
        User user = null;
        if (userService.findByEmail(userRequest.getEmail()) != null) {
            throw new EntityExistsException("user already exists with this email");
        }

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
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("login")
    public String login(@Valid @RequestBody UserLoginRequest userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(userRequest.getEmail());
            }
        } catch (AuthenticationException ex) {
            System.out.println("Authentication failed: " + ex.getMessage());
            throw ex;
        }
        return "Login Failed";
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