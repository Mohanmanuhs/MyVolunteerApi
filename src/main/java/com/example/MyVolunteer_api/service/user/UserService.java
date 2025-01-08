package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.dto.ChangePassDto;
import com.example.MyVolunteer_api.model.user.User;

public interface UserService {

    User createUser(User user);

    User changePassword(ChangePassDto changePassDto);

    User updateUser(User user);

    User findById(Integer id);

    void deleteUser(User user);

    User findByEmail(String email);
}
