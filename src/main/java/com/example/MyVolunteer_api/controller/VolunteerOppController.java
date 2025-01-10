package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.VolOppSaveDto;
import com.example.MyVolunteer_api.dto.VolunteerOpportunitiesDTO;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.model.user.User;
import com.example.MyVolunteer_api.service.task.VolunteerOppService;
import com.example.MyVolunteer_api.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<List<VolunteerOpportunitiesDTO>> getAllTask() {
        List<VolunteerOpportunitiesDTO> list = volunteerOppService.getAllTask();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@Valid @RequestBody VolOppSaveDto volOppSaveDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        VolunteerOpportunities volunteerOpportunities = null;

        if (user.getRole() == Role.ORGANIZATION) {
            volunteerOpportunities = volOppDtoToVolOpp(volOppSaveDto,new VolunteerOpportunities());
            volunteerOpportunities.setCreatedBy((Organization) user);
            volunteerOpportunities.setOrganization_name(user.getName());
            volunteerOpportunities = volunteerOppService.createTask(volunteerOpportunities);
        }

        assert volunteerOpportunities != null;
        return ResponseEntity.ok(volOppToVolOppDto(volunteerOpportunities,new VolunteerOpportunitiesDTO()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTask(@Valid @RequestBody VolunteerOpportunitiesDTO volunteerOpportunitiesDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        VolunteerOpportunities volunteerOpportunities = volunteerOppService.findById(volunteerOpportunitiesDTO.getTaskId())
                .orElseThrow();

        if (user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user) {
            volOppDtoToVolOpp(volunteerOpportunitiesDTO, volunteerOpportunities);
            volunteerOpportunities = volunteerOppService.updateTask(volunteerOpportunities);
        }

        return ResponseEntity.ok(volOppToVolOppDto(volunteerOpportunities,new VolunteerOpportunitiesDTO()));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        VolunteerOpportunities volunteerOpportunities = volunteerOppService.findById(id).orElseThrow();

        if (user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user) {
            volunteerOppService.deleteTask(id);
            return ResponseEntity.ok("Deleted successfully");
        }

        return ResponseEntity.ok("unAuthorized");
    }

    private VolunteerOpportunities volOppDtoToVolOpp(VolOppSaveDto volOppSaveDto,VolunteerOpportunities volunteerOpportunities) {
        volunteerOpportunities.setTitle(volOppSaveDto.getTitle());
        volunteerOpportunities.setDescription(volOppSaveDto.getDescription());
        volunteerOpportunities.setLocation(volOppSaveDto.getLocation());
        volunteerOpportunities.setSkills_required(volOppSaveDto.getSkillsRequired());
        volunteerOpportunities.setDeadLineForReg(volOppSaveDto.getDeadLineForReg());
        volunteerOpportunities.setStartsAt(volOppSaveDto.getStartsAt());
        volunteerOpportunities.setEndsAt(volOppSaveDto.getEndsAt());
        volunteerOpportunities.setStatus(volOppSaveDto.getStatus());
        return volunteerOpportunities;
    }

    private VolunteerOpportunities volOppDtoToVolOpp(VolunteerOpportunitiesDTO volOppSaveDto,VolunteerOpportunities volunteerOpportunities) {
        volunteerOpportunities.setTitle(volOppSaveDto.getTitle());
        volunteerOpportunities.setDescription(volOppSaveDto.getDescription());
        volunteerOpportunities.setLocation(volOppSaveDto.getLocation());
        volunteerOpportunities.setSkills_required(volOppSaveDto.getSkillsRequired());
        volunteerOpportunities.setDeadLineForReg(volOppSaveDto.getDeadLineForReg());
        volunteerOpportunities.setStartsAt(volOppSaveDto.getStartsAt());
        volunteerOpportunities.setEndsAt(volOppSaveDto.getEndsAt());
        volunteerOpportunities.setStatus(volOppSaveDto.getStatus());
        return volunteerOpportunities;
    }

    private VolunteerOpportunitiesDTO volOppToVolOppDto(VolunteerOpportunities volunteerOpportunities,VolunteerOpportunitiesDTO volunteerOpportunitiesDTO) {
        volunteerOpportunitiesDTO.setTaskId(volunteerOpportunities.getTaskId());
        volunteerOpportunitiesDTO.setTitle(volunteerOpportunities.getTitle());
        volunteerOpportunitiesDTO.setDescription(volunteerOpportunities.getDescription());
        volunteerOpportunitiesDTO.setLocation(volunteerOpportunities.getLocation());
        volunteerOpportunitiesDTO.setSkillsRequired(volunteerOpportunities.getSkills_required());
        volunteerOpportunitiesDTO.setOrganizationName(volunteerOpportunities.getOrganization_name());
        volunteerOpportunitiesDTO.setDeadLineForReg(volunteerOpportunities.getDeadLineForReg());
        volunteerOpportunitiesDTO.setStartsAt(volunteerOpportunities.getStartsAt());
        volunteerOpportunitiesDTO.setEndsAt(volunteerOpportunities.getEndsAt());
        volunteerOpportunitiesDTO.setStatus(volunteerOpportunities.getStatus());
        return volunteerOpportunitiesDTO;
    }


}
