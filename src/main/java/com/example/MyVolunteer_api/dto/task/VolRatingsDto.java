package com.example.MyVolunteer_api.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolRatingsDto {

    @Max(5)
    @Min(0)
    @NotNull(message = "rating may not be null")
    private Integer rating;

    @NotEmpty(message = "please provide name of organization")
    private String givenBy;

    private String feedback;
}
