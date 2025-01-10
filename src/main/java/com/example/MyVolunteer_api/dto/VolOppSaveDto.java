package com.example.MyVolunteer_api.dto;


import com.example.MyVolunteer_api.constants.OpportunityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolOppSaveDto {
    @NotBlank(message = "Title may not be blank")
    private String title;

    private String description;

    @NotBlank(message = "Location may not be blank")
    private String location;

    @NotEmpty(message = "Skills are required")
    private List<String> skillsRequired;

    @NotNull(message = "deadLineForReg may not be null")
    private Date deadLineForReg;

    @NotNull(message = "startsAt may not be null")
    private Date startsAt;

    @NotNull(message = "endsAt may not be null")
    private Date endsAt;

    @NotNull(message = "Status may not be null")
    private OpportunityStatus status;

}
