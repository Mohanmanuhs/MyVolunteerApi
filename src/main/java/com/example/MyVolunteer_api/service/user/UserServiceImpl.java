package com.example.MyVolunteer_api.service.user;

import com.example.MyVolunteer_api.dto.ChangePassDto;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.repository.user.UserRepo;
import com.example.MyVolunteer_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    @Override
    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Generate a 6-digit OTP
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Generate OTP, save it to the user, and send via email
    @Override
    public void generateAndSendOTP(User user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes
        userRepo.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    public User changePassword(ChangePassDto changePassDto) {
        User user = userRepo.findByEmail(changePassDto.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("user with this email not found");
        } else if (!encoder.matches(changePassDto.getOldPassword(), user.getPassword())) {
            throw new InputMismatchException("password don't match old password");
        }
        user.setPassword(encoder.encode(changePassDto.getNewPassword()));
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

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void deleteUserByEmail(String email) {
        userRepo.deleteByEmail(email);
    }
}
