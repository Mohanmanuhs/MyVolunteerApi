package com.example.MyVolunteer_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "volunteer_opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerOpportunities {

    private Integer taskId;

    private String title;

    private String description;

    private String location;

    private List<String> skills_required;

    private String organization_name;

    private Date deadLineForReg;

    private Date startsAt;

    private Date endsAt;

    private Integer createdBy;

    private Status status;

}
