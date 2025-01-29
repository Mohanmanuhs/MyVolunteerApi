package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.constants.SignUpStatus;
import com.example.MyVolunteer_api.dto.SignupForVolDto;
import com.example.MyVolunteer_api.dto.VolunteerOpportunitiesDTO;
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

    public Optional<TaskSignups> getSignupByVolunteerAndTask(Volunteer volunteer, VolunteerOpportunities volunteerOpportunities) {
        return taskSignupsRepo.getSignupByVolunteerAndTask(volunteer, volunteerOpportunities);
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
                signup.getStatus())).collect(Collectors.toList());
    }

    public boolean isVolRegForTask(Volunteer volunteer, VolunteerOpportunities volunteerOpportunities) {
        return taskSignupsRepo.existsByVolunteerAndTaskAndStatus(volunteer,volunteerOpportunities, SignUpStatus.TAKEN);
    }

    public List<VolunteerOpportunitiesDTO> getAllTaskSignupsByVolunteer(Volunteer volunteer) {
        return taskSignupsRepo.findTasksByVolunteer(volunteer,SignUpStatus.TAKEN).stream().map(task -> new VolunteerOpportunitiesDTO(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getLocation(),
                task.getSkills_required(),
                task.getOrganization_name(),
                task.getDeadLineForReg(),
                task.getStartsAt(),
                task.getEndsAt(),task.getStatus()
        )).collect(Collectors.toList());
    }

    public void cancelSignUp(Volunteer volunteer, VolunteerOpportunities task) {
        int updatedRows = taskSignupsRepo.cancelSignUpByVolunteerAndTask(SignUpStatus.CANCELLED, volunteer, task);
        if (updatedRows == 0) {
            throw new RuntimeException("No signup found to cancel");
        }
    }

    public List<TaskSignups> getAllSignupsByTask(VolunteerOpportunities task) {
        return taskSignupsRepo.findByTask(task);
    }

    public Optional<TaskSignups> findById(Integer taskId) {
        return taskSignupsRepo.findById(taskId);
    }
}
