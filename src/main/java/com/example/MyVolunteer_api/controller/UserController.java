package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
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

}
