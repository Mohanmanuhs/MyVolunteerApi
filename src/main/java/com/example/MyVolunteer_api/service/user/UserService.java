package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.dto.auth.ChangePassDto;
import com.example.MyVolunteer_api.model.user.User;

public interface UserService {

    void createUser(User user);

    void generateAndSendOTP(User user);

    void changePassword(ChangePassDto changePassDto);

    User updateUser(User user);

    User findById(Integer id);

    void deleteUser(User user);

    User findByEmail(String email);

    void deleteUserByEmail(String email);
}
