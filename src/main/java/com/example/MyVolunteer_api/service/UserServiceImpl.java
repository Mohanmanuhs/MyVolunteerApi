package com.example.MyVolunteer_api.service;

import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User changePassword(User user, String newPassword) {
        User user1 = findById(user.getId());
        user1.setPassword(newPassword);
        return userRepository.save(user1);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
