package com.example.MyVolunteer_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerRatingRequest {
    @Max(5)
    @Min(0)
    private Integer rating;

    private String feedback;

}