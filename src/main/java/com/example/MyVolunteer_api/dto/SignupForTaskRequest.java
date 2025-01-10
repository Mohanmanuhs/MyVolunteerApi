package com.example.MyVolunteer_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupForTaskRequest {

    @NotNull(message = "taskId must not be null")
    private Integer taskId;
}
