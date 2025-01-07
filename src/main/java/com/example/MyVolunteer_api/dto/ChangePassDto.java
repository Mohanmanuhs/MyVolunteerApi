package com.example.MyVolunteer_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassDto {

    private String email;

    private String oldPassword;

    private String newPassword;

}
