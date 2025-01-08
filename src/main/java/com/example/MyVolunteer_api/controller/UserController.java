package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.dto.ChangePassDto;
import com.example.MyVolunteer_api.dto.UserLoginRequest;
import com.example.MyVolunteer_api.dto.UserRegisterRequest;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.JwtService;
import com.example.MyVolunteer_api.service.user.OrganizationService;
import com.example.MyVolunteer_api.service.user.UserService;
import com.example.MyVolunteer_api.service.user.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public ResponseEntity<User> createUser(@RequestBody UserRegisterRequest userRequest) {
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
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("login")
    public String login(@RequestBody UserLoginRequest userRequest) {
        System.out.println("login: " + userRequest);
        System.out.println(new BCryptPasswordEncoder(12).encode(userRequest.getPassword()));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
            );
            System.out.println("login: " + authentication);
            if (authentication.isAuthenticated())
                return jwtService.generateToken(userRequest.getEmail());
        } catch (AuthenticationException ex) {
            System.out.println("Authentication failed: " + ex.getMessage());
            throw ex; // Re-throw the exception if needed
        }

        return "Login Failed";

    }


    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody ChangePassDto changePassDto) {
        return ResponseEntity.ok(userService.changePassword(changePassDto));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
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
