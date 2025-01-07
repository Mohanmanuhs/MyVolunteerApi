package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.dto.ChangePassDto;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User changePassword(ChangePassDto changePassDto) {
        User user = userRepo.findByEmail(changePassDto.getEmail());
        if (user == null || !Objects.equals(user.getPassword(), changePassDto.getOldPassword())) {
            return null;
        }
        user.setPassword(changePassDto.getNewPassword());
        return userRepo.save(user);
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
