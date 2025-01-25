package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.constants.OpportunityStatus;
import com.example.MyVolunteer_api.constants.Role;
import com.example.MyVolunteer_api.dto.VolOppSaveDto;
import com.example.MyVolunteer_api.dto.VolunteerOpportunitiesDTO;
import com.example.MyVolunteer_api.model.UserPrincipal;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/volunteerOpp")
public class VolunteerOppController {

    @Autowired
    private VolunteerOppService volunteerOppService;

    @Autowired
    private TaskSignupsService taskSignupsService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String searchVolOpp(
            @RequestParam(value = "filterBy", required = false) String filterBy,
            @RequestParam(value = "searchText", required = false) String searchText,
            Model model) {

        List<VolunteerOpportunitiesDTO> searchResults;

        // Call the service to get filtered results
        if (filterBy != null && searchText != null && !searchText.isEmpty()) {
            searchResults = volunteerOppService.searchVolOpp(filterBy, searchText);
        } else {
            searchResults = volunteerOppService.getAllTask();
        }

        model.addAttribute("volunteerOpportunities", searchResults);
        model.addAttribute("user", "Vol");
        return "VolunteerOpp";
    }

    @GetMapping("/creations")
    public String getAllTaskForOrg(@RequestParam(value = "filterBy", required = false) String filterBy,
                                   @RequestParam(value = "searchText", required = false) String searchText,
                                   Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:VolunteerOpp"; // Redirect with error message
        }
        if (user.getRole() == Role.ORGANIZATION) {
            List<VolunteerOpportunitiesDTO> list = volunteerOppService.searchVolOppForOrg((Organization) user, filterBy, searchText);
            model.addAttribute("volunteerOpportunities", list);
            model.addAttribute("user", "Org");
            return "VolunteerOpp";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "not a organization");
        return "redirect:VolunteerOpp"; // Redirect with error message

    }

    @GetMapping("/{uiName}/{id}")
    public String getTaskById(Model model, RedirectAttributes redirectAttributes,@PathVariable("uiName") String uiName,@PathVariable("id") Integer id) {
        Optional<VolunteerOpportunities> volunteerOpportunities = volunteerOppService.findById(id);
        if (volunteerOpportunities.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "not a organization");
            return "redirect:/test/create";
        }
        VolunteerOpportunitiesDTO volunteerOpportunitiesDTO = volOppToVolOppDto(volunteerOpportunities.get(), new VolunteerOpportunitiesDTO());
        model.addAttribute("volOpp", volunteerOpportunitiesDTO);
        if(uiName.equals("update")){
            model.addAttribute("statuses", OpportunityStatus.values());
        }else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            String email = userDetails.getUsername();
            User user = userService.findByEmail(email);
            if (user != null && user.getRole()==Role.VOLUNTEER) {
                boolean isReg = taskSignupsService.isVolRegForTask((Volunteer) user,volunteerOpportunities.get());
                model.addAttribute("isRegistered", isReg);
            }
        }
        return uiName+"VolOpp";
    }

    @PostMapping("/create")
    public String createTask(@Valid @ModelAttribute VolOppSaveDto volOppSaveDto, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/test/create"; // Redirect with error message
        }
        VolunteerOpportunities volunteerOpportunities = null;

        if (user.getRole() == Role.ORGANIZATION) {
            volunteerOpportunities = volOppDtoToVolOpp(volOppSaveDto, new VolunteerOpportunities());
            volunteerOpportunities.setCreatedBy((Organization) user);
            volunteerOpportunities.setOrganization_name(user.getName());
            volunteerOpportunities = volunteerOppService.createTask(volunteerOpportunities);
        }

        assert volunteerOpportunities != null;
        redirectAttributes.addFlashAttribute("successMessage", "Volunteer opportunity created successfully!");
        return "redirect:/test/create"; // Redirect with success message
    }

    @PostMapping("/update")
    public String updateTask(@Valid @ModelAttribute VolunteerOpportunitiesDTO volunteerOpportunitiesDTO, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.findByEmail(email);
        if (user == null) {
            return "user not found";
        }

        VolunteerOpportunities volunteerOpportunities = volunteerOppService.findById(volunteerOpportunitiesDTO.getTaskId())
                .orElseThrow();

        if (user.getRole() == Role.ORGANIZATION && volunteerOpportunities.getCreatedBy() == user) {
            volOppDtoToVolOpp(volunteerOpportunitiesDTO, volunteerOpportunities);
            volunteerOpportunities = volunteerOppService.updateTask(volunteerOpportunities);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Volunteer opportunity updated successfully!");
        return "redirect:/volunteerOpp/update" + volunteerOpportunities.getTaskId();
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

    private VolunteerOpportunities volOppDtoToVolOpp(VolOppSaveDto volOppSaveDto, VolunteerOpportunities
            volunteerOpportunities) {
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

    private VolunteerOpportunities volOppDtoToVolOpp(VolunteerOpportunitiesDTO
                                                             volOppSaveDto, VolunteerOpportunities volunteerOpportunities) {
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

    private VolunteerOpportunitiesDTO volOppToVolOppDto(VolunteerOpportunities
                                                                volunteerOpportunities, VolunteerOpportunitiesDTO volunteerOpportunitiesDTO) {
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
