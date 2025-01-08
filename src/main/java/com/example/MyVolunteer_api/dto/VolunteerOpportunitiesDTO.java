package com.example.MyVolunteer_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerOpportunitiesDTO {
    private Integer taskId;
    private String title;
    private String description;
    private String location;
    private List<String> skillsRequired;
    private String organizationName;
    private Date deadLineForReg;
    private Date startsAt;
    private Date endsAt;
}