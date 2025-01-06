package com.example.MyVolunteer_api.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRatingId implements Serializable {

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "volunteer_id")
    private Integer volunteerId;

    @Column(name = "organization_id")
    private Integer organizationId;
}