package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.model.user.User;

public interface UserService {

    User createUser(User user);

    User changePassword(User user,String newPassword);

    User updateUser(User user);

    User findById(Integer id);

    void deleteUser(User user);

}
