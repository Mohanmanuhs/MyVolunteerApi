package com.example.MyVolunteer_api.model.task;


import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.Volunteer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "task_ratings")
public class TaskRatings {

    @EmbeddedId
    private TaskRatingId id;


    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private VolunteerOpportunities task;


    @ManyToOne
    @MapsId("volunteerId")
    @JoinColumn(name = "volunteer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Volunteer volunteer;


    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization;

    @Max(5)
    @Min(0)
    private Integer ratingByOrg;

    @Max(5)
    @Min(0)
    private Integer ratingByVol;

    @Column(columnDefinition = "TEXT")
    private String feedbackByOrg;

    @Column(columnDefinition = "TEXT")
    private String feedbackByVol;

}
