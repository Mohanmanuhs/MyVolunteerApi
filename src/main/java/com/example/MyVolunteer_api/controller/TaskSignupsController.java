package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.constants.SignUpStatus;
import com.example.MyVolunteer_api.dto.SignupForTaskDto;
import com.example.MyVolunteer_api.dto.SignupForTaskRequest;
import com.example.MyVolunteer_api.dto.SignupForVolDto;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.task.TaskSignupsService;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import com.example.MyVolunteer_api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taskSignups")
public class TaskSignupsController {


    @Autowired
    private TaskSignupsService taskSignupsService;

    @Autowired
    private VolunteerOppService volunteerOppService;

    @Autowired
    private UserService userService;


    @GetMapping("/getAllForVol")
    public ResponseEntity<?> getAllSignupsForVolunteer() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<SignupForVolDto> list = Collections.emptyList();
        if (user.getRole() == Role.VOLUNTEER) {
            list = taskSignupsService.getAllSignupsByVolunteer((Volunteer) user);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getAllForTask")
    public ResponseEntity<?> getAllSignupsForTask(@Valid  @RequestBody SignupForTaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        VolunteerOpportunities task = volunteerOppService.findById(taskRequest.getTaskId()).orElseThrow();
        if (user.getRole() == Role.ORGANIZATION && task.getCreatedBy() == user) {
            return ResponseEntity.ok(task.getTaskSignups().stream().map(
                    this::taskSignupToDto
            ).collect(Collectors.toList()));
        }
        return ResponseEntity.ok("failed to fetch");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTaskSignup(@Valid @RequestBody SignupForTaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        VolunteerOpportunities task = volunteerOppService.findById(taskRequest.getTaskId()).orElseThrow();

        TaskSignups taskSignups = new TaskSignups();
        if (user.getRole() == Role.VOLUNTEER) {
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
        return ResponseEntity.ok(taskSignupToDto(taskSignups));
    }

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelTaskSignup(@Valid @RequestBody SignupForTaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        TaskSignups taskSignups = taskSignupsService.findById(taskRequest.getTaskId()).orElseThrow();

        if (user.getRole() == Role.VOLUNTEER && taskSignups.getVolunteer() == user) {
            taskSignups.setStatus(SignUpStatus.CANCELLED);
            taskSignupsService.updateSignUp(taskSignups);
            return ResponseEntity.ok("cancelled successfully");
        }
        return ResponseEntity.ok("error");
    }

    private SignupForTaskDto taskSignupToDto(TaskSignups taskSignups) {
        SignupForTaskDto taskSignup = new SignupForTaskDto();
        taskSignup.setTaskDesc(taskSignups.getTaskDesc());
        taskSignup.setTaskTitle(taskSignups.getTaskTitle());
        taskSignup.setName(taskSignups.getName());
        taskSignup.setEmail(taskSignups.getEmail());
        taskSignup.setStatus(taskSignups.getStatus());
        taskSignup.setAssignedDate(taskSignups.getAssignedDate());
        taskSignup.setCompletionDate(taskSignups.getCompletionDate());
        return taskSignup;

    }

}
