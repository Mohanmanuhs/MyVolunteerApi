package com.example.MyVolunteer_api.dto;

import com.example.MyVolunteer_api.constants.Gender;
import com.example.MyVolunteer_api.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String name;
    private String password;
    private String phone;
    private Gender gender;
    private Role role;

    // Organization-specific fields
    private String gstNumber;
    private String location;

    // Volunteer-specific fields
    private List<String> skills;
}