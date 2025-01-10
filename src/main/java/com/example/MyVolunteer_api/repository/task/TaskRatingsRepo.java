package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRatingsRepo extends JpaRepository<TaskRatings,Integer> {
    Optional<TaskRatings> findByTaskAndVolunteer(VolunteerOpportunities task, Volunteer volunteer);

    List<TaskRatings> findByVolunteer(Volunteer user);

    List<TaskRatings> findByOrganization(Organization user);
}
