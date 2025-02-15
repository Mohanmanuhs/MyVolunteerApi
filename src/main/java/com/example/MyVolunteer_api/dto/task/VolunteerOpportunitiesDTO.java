package com.example.MyVolunteer_api.dto.task;

import com.example.MyVolunteer_api.constants.OpportunityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerOpportunitiesDTO {

    @NotNull(message = "taskId must not be null")
    private Integer taskId;

    @NotBlank(message = "Title may not be blank")
    private String title;

    private String description;

    @NotBlank(message = "location may not be blank")
    private String location;

    @NotEmpty(message = "Skills are required")
    private List<String> skillsRequired;

    private String organizationName;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "deadLineForReg may not be null")
    private Date deadLineForReg;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "startsAt may not be null")
    private Date startsAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "endsAt may not be null")
    private Date endsAt;

    @NotNull(message = "Status may not be null")
    private OpportunityStatus status;

}