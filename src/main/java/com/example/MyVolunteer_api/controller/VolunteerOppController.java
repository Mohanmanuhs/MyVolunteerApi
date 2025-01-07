package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteerOpp")
public class VolunteerOppController {

    @Autowired
    private VolunteerOppService volunteerOppService;

    @GetMapping("/getAll")
    public ResponseEntity<List<VolunteerOpportunities>> getAllTask() {

        List<VolunteerOpportunities> list = volunteerOppService.getAllTask();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/create")
    public ResponseEntity<VolunteerOpportunities> createTask(@RequestBody VolunteerOpportunities volunteerOpportunities) {

        User user = null; //get user by securityContext

        VolunteerOpportunities volunteerOpportunities1 = null;
        if(user.getRole() == Role.ORGANIZATION){
            volunteerOpportunities.setCreatedBy((Organization) user);
            volunteerOpportunities.setOrganization_name(user.getName());
            volunteerOpportunities1 = volunteerOppService.createTask(volunteerOpportunities);
        }

        return ResponseEntity.ok(volunteerOpportunities1);
    }

    @PutMapping("/update")
    public ResponseEntity<VolunteerOpportunities> updateTask(@RequestBody VolunteerOpportunities volunteerOpportunities) {

        User user = null; //get user by securityContext

        VolunteerOpportunities volunteerOpportunities1 = null;
        if(user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user){
            volunteerOpportunities1 = volunteerOppService.updateTask(volunteerOpportunities);
        }

        return ResponseEntity.ok(volunteerOpportunities1);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {

        User user = null; //get user by securityContext

        VolunteerOpportunities volunteerOpportunities = volunteerOppService.findById(id);

        if(user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user){
            volunteerOppService.deleteTask(id);
            return ResponseEntity.ok("Deleted successfully");
        }

        return ResponseEntity.ok("unAuthorized");
    }

}
