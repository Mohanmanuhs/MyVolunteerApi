package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.model.task.TaskRatings;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.task.TaskRatingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskRatingsService {

    @Autowired
    private TaskRatingsRepo taskRatingsRepo;


    public boolean existsByTaskAndVolunteer(VolunteerOpportunities task, Volunteer volunteer) {
        return taskRatingsRepo.existsByTaskAndVolunteer(task, volunteer);
    }


    public Optional<TaskRatings> findByTaskAndVolunteer(VolunteerOpportunities task, Volunteer volunteer) {
        return taskRatingsRepo.findByTaskAndVolunteer(task, volunteer);
    }

    public void updateRatings(TaskRatings taskRating) {
        taskRatingsRepo.save(taskRating);
    }
}
