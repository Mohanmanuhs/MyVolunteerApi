package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.user.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskSignupsRepo extends JpaRepository<TaskSignups,Integer> {

    List<TaskSignups> findByVolunteer(Volunteer volunteer);
}
