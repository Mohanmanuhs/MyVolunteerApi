package com.example.MyVolunteer_api.dto.task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupOrgDto {

    @NotNull(message = "signupId may not be blank")
    private Integer signUpId;

    @NotBlank(message = "name may not be blank")
    private String name;

    @NotBlank(message = "email may not be blank")
    @Email(message = "Email must be valid")
    private String email;

}
