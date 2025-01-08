package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.constants.SignUpStatus;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.task.TaskSignupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/taskSignups")
public class TaskSignupsController {


    @Autowired
    private TaskSignupsService taskSignupsService;


    @GetMapping("/getAllForVol")
    public ResponseEntity<List<TaskSignups>> getAllSignupsForVolunteer(@AuthenticationPrincipal User user) {

        List<TaskSignups> list = Collections.emptyList();
        if(user.getRole() == Role.VOLUNTEER){
            list = taskSignupsService.getAllSignupsByVolunteer((Volunteer) user);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getAllForTask")
    public ResponseEntity<List<TaskSignups>> getAllSignupsForTask(@RequestBody VolunteerOpportunities task,@AuthenticationPrincipal User user) {

        List<TaskSignups> list = Collections.emptyList();
        if(user.getRole() == Role.ORGANIZATION){
            list = taskSignupsService.getAllSignupsByTask(task);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    public ResponseEntity<TaskSignups> createTaskSignup(@RequestBody VolunteerOpportunities task,@AuthenticationPrincipal User user) {

        TaskSignups taskSignups = new TaskSignups();
        if(user.getRole() == Role.VOLUNTEER){
            taskSignups.setVolunteer((Volunteer) user);
            taskSignups.setTask(task);
            taskSignups.setName(user.getName());
            taskSignups.setEmail(user.getEmail());
            taskSignups.setStatus(SignUpStatus.TAKEN);
            taskSignups.setTaskTitle(task.getTitle());
            taskSignups.setTaskDesc(task.getDescription());
            taskSignups.setOrganizedBy(task.getOrganization_name());
            taskSignups.setAssignedDate(task.getStartsAt());
            taskSignups.setCompletionDate(task.getEndsAt());

            taskSignups = taskSignupsService.createSignUp(taskSignups);
        }
        return ResponseEntity.ok(taskSignups);
    }

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelTaskSignup(@RequestBody TaskSignups taskSignups,@AuthenticationPrincipal User user) {

        if(user.getRole() == Role.VOLUNTEER && taskSignups.getVolunteer() == user){
            taskSignups.setStatus(SignUpStatus.CANCELLED);
            taskSignupsService.updateSignUp(taskSignups);
            return ResponseEntity.ok("cancelled");
        }
        return ResponseEntity.ok("error");
    }


}
