package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.model.task.TaskSignups;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.model.user.Volunteer;
import com.example.MyVolunteer_api.service.task.TaskSignupsService;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/taskSignups")
public class TaskSignupsController {


    @Autowired
    private TaskSignupsService taskSignupsService;


    @GetMapping("/getAll")
    public ResponseEntity<List<TaskSignups>> getAllTask() {

        User user = null; //get user by securityContext
        List<TaskSignups> list = Collections.emptyList();
        if(user.getRole() == Role.VOLUNTEER){
             list = taskSignupsService.getAllTask((Volunteer) user);
        }
        return ResponseEntity.ok(list);
    }


    /*@PostMapping("/create")
    public ResponseEntity<TaskSignups> createTaskSignup() {
        return ResponseEntity.ok();
    }*/


}
