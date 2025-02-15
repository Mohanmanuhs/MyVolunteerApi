package com.example.MyVolunteer_api.dto.task;

import com.example.MyVolunteer_api.constants.SignUpStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupForTaskDto {

    @NotBlank(message = "name may not be blank")
    private String name;

    @NotBlank(message = "email may not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Title may not be blank")
    private String taskTitle;

    private String taskDesc;

    @NotNull(message = "assignedDate may not be null")
    private Date assignedDate;

    @NotNull(message = "completionDate may not be null")
    private Date completionDate;

    @NotNull(message = "SignUpStatus may not be null")
    private SignUpStatus status;

}
