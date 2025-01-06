package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.dto.UserRequest;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.user.OrganizationService;
import com.example.MyVolunteer_api.service.user.UserService;
import com.example.MyVolunteer_api.service.user.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private VolunteerService volunteerService;


    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        User user = null;
        switch (userRequest.getRole()){
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


    @PutMapping("/changePassword/{newPassword}")
    public ResponseEntity<User> changePassword(@RequestBody User user,@PathVariable String newPassword) {
        return ResponseEntity.ok(userService.changePassword(user, newPassword));
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
    public ResponseEntity<Void> deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }


    private static Volunteer getVolunteer(UserRequest userRequest) {
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

    private static Organization getOrganization(UserRequest userRequest) {
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
