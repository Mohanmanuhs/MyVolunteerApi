package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.model.User;
import com.example.MyVolunteer_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/edit")
    public User editUser(@RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/UpdatePassword")
    public String updatePassword(@RequestBody User user, String newPassword) {
        userService.changePassword(user, newPassword);
        return "Password updated successfully";
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody User user) {
        return ResponseEntity.ok("User deleted successfully");
    }


}
