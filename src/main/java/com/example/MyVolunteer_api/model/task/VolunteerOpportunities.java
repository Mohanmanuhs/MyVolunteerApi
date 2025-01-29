package com.example.MyVolunteer_api.model.task;

import com.example.MyVolunteer_api.constants.OpportunityStatus;
import com.example.MyVolunteer_api.model.user.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "volunteer_opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerOpportunities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String location;

    @ElementCollection
    @CollectionTable(name = "vo_skills", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "skill_required")
    private List<String> skills_required;

    @Column(nullable = false)
    private String organization_name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadLineForReg;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startsAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endsAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Organization createdBy;

    @Column(nullable = false)
    private OpportunityStatus status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskSignups> taskSignups = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskRatings> ratings = new ArrayList<>();


    public OpportunityStatus getStatus() {
        if (new Date().after(deadLineForReg) && new Date().after(endsAt)) {
            setStatus(OpportunityStatus.COMPLETED);
        } else if (new Date().after(deadLineForReg)) {
            setStatus(OpportunityStatus.CLOSED);
        }
        return status;
    }
}
