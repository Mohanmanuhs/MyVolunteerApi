package com.example.MyVolunteer_api.dto;

import com.example.MyVolunteer_api.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    private String email;

    private String password;

    private Role role;

}
