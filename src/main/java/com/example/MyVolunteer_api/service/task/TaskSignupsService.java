package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.dto.SignupForVolDto;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.repository.task.TaskSignupsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<SignupForVolDto> getAllSignupsByVolunteer(Volunteer volunteer) {
        return taskSignupsRepo.findByVolunteer(volunteer).stream().map(signup -> new SignupForVolDto(
                signup.getSignupId(),
                signup.getTaskTitle(),
                signup.getTaskDesc(),
                signup.getOrganizedBy(),
                signup.getAssignedDate(),
                signup.getCompletionDate(),
                signup.getStatus()
        )).collect(Collectors.toList());
    }

    public List<TaskSignups> getAllSignupsByTask(VolunteerOpportunities task) {
        return taskSignupsRepo.findByTask(task);
    }

    public Optional<TaskSignups> findById(Integer taskId) {
        return taskSignupsRepo.findById(taskId);
    }
}
