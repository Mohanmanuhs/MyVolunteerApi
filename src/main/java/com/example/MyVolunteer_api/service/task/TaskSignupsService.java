package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.task.TaskSignupsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSignupsService {

    @Autowired
    private TaskSignupsRepo taskSignupsRepo;

    public TaskSignups createSignUp(TaskSignups taskSignups) {
        return taskSignupsRepo.save(taskSignups);
    }

    public void updateSignUp(TaskSignups taskSignups) {
        taskSignupsRepo.save(taskSignups);
    }

    public List<TaskSignups> getAllSignupsByVolunteer(Volunteer volunteer) {
        return taskSignupsRepo.findByVolunteer(volunteer);
    }

    public List<TaskSignups> getAllSignupsByTask(VolunteerOpportunities task) {
        return taskSignupsRepo.findByTask(task);
    }
}
