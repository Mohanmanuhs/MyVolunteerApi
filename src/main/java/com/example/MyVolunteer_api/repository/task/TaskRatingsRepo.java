package com.example.MyVolunteer_api.repository.task;

import com.example.MyVolunteer_api.model.task.TaskRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRatingsRepo extends JpaRepository<TaskRatings,Integer> {

}
