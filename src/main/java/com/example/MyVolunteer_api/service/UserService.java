package com.example.MyVolunteer_api.service;


import com.example.MyVolunteer_api.model.User;

public interface UserService {

    // Create a user
    User create(User user);

    // Change password
    void changePassword(User user, String newPasword);

    // Update user
    User update(User user);

    // Delete user
    void delete(User user);

    User findbyId(int id);
}
