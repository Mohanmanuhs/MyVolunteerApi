package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User changePassword(User user, String newPassword) {
        User user1 = findById(user.getId());
        user1.setPassword(newPassword);
        return userRepo.save(user1);
    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepo.findById(id).orElseThrow();
    }

    @Override
    public void deleteUser(User user) {
        userRepo.delete(user);
    }
}
