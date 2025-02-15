package com.example.MyVolunteer_api.dto.task;

import com.example.MyVolunteer_api.constants.SignUpStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupForVolDto {

    @NotNull(message = "signUpId must not be null")
    private Integer signUpId;

    @NotBlank(message = "taskTitle may not be blank")
    private String taskTitle;

    private String taskDesc;

    @NotBlank(message = "organizedBy may not be blank")
    private String organizedBy;

    @NotNull(message = "assignedDate may not be null")
    private Date assignedDate;

    @NotNull(message = "completionDate may not be null")
    private Date completionDate;

    @NotNull(message = "SignUpStatus may not be null")
    private SignUpStatus status;

}
