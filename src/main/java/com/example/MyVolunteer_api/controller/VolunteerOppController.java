package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.VolunteerOpportunitiesDTO;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import com.example.MyVolunteer_api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteerOpp")
public class VolunteerOppController {

    @Autowired
    private VolunteerOppService volunteerOppService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseEntity<List<VolunteerOpportunities>> getAllTask() {

        List<VolunteerOpportunities> list = volunteerOppService.getAllTask();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/create")
    public ResponseEntity<VolunteerOpportunitiesDTO> createTask(@RequestBody VolunteerOpportunities volunteerOpportunities) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);

        VolunteerOpportunities volunteerOpportunities1 = null;
        if(user.getRole() == Role.ORGANIZATION){
            volunteerOpportunities.setCreatedBy((Organization) user);
            volunteerOpportunities.setOrganization_name(user.getName());
            volunteerOpportunities1 = volunteerOppService.createTask(volunteerOpportunities);
        }

        assert volunteerOpportunities1 != null;
        VolunteerOpportunitiesDTO dto = new VolunteerOpportunitiesDTO(
                volunteerOpportunities1.getTaskId(),
                volunteerOpportunities1.getTitle(),
                volunteerOpportunities1.getDescription(),
                volunteerOpportunities1.getLocation(),
                volunteerOpportunities1.getSkills_required(),
                volunteerOpportunities1.getOrganization_name(),
                volunteerOpportunities1.getDeadLineForReg(),
                volunteerOpportunities1.getStartsAt(),
                volunteerOpportunities1.getEndsAt()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<VolunteerOpportunities> updateTask(@RequestBody VolunteerOpportunities volunteerOpportunities,@AuthenticationPrincipal User user) {

        VolunteerOpportunities volunteerOpportunities1 = null;
        if(user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user){
            volunteerOpportunities1 = volunteerOppService.updateTask(volunteerOpportunities);
        }

        return ResponseEntity.ok(volunteerOpportunities1);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id,@AuthenticationPrincipal User user) {

        VolunteerOpportunities volunteerOpportunities = volunteerOppService.findById(id).orElseThrow();

        if(user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user){
            volunteerOppService.deleteTask(id);
            return ResponseEntity.ok("Deleted successfully");
        }

        return ResponseEntity.ok("unAuthorized");
    }

}
