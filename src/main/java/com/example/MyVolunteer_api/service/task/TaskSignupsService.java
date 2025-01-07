package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.task.TaskSignupsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSignupsService {

    @Autowired
    private TaskSignupsRepo taskSignupsRepo;

    public List<TaskSignups> getAllTask(Volunteer volunteer) {
        return taskSignupsRepo.findByVolunteer(volunteer);
    }
}
