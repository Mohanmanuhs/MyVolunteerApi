package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.repository.task.TaskSignupsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskSignupsService {

    @Autowired
    private TaskSignupsRepo taskSignupsRepo;
}
