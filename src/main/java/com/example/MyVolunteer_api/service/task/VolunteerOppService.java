package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.dto.task.VolunteerOpportunitiesDTO;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.model.user.Organization;
import com.example.MyVolunteer_api.repository.task.VolunteerOppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerOppService {

    @Autowired
    private VolunteerOppRepo volunteerOppRepo;


    public List<VolunteerOpportunitiesDTO> getAllTask() {
        return volunteerOppRepo.findAll().stream().map(task -> new VolunteerOpportunitiesDTO(task.getTaskId(), task.getTitle(), task.getDescription(), task.getLocation(), task.getSkills_required(), task.getOrganization_name(), task.getDeadLineForReg(), task.getStartsAt(), task.getEndsAt(), task.getStatus())).collect(Collectors.toList());
    }

    public List<VolunteerOpportunitiesDTO> searchVolOppForOrg(Organization organization, String filterBy, String searchText) {

        List<VolunteerOpportunitiesDTO> tasks = getAllTaskForOrg(organization);

        if (filterBy == null || searchText == null || searchText.isEmpty()) {
            return tasks;
        }

        switch (filterBy.toLowerCase()) {
            case "title":
                tasks = tasks.stream().filter(task -> task.getTitle() != null && task.getTitle().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                break;

            case "location":
                tasks = tasks.stream().filter(task -> task.getLocation() != null && task.getLocation().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
                break;

            case "skills":
                tasks = tasks.stream().filter(task -> task.getSkillsRequired() != null && task.getSkillsRequired().stream().anyMatch(skill -> skill.equalsIgnoreCase(searchText))).collect(Collectors.toList());
                break;

            case "date":
                tasks = tasks.stream().filter(task -> task.getStartsAt() != null && task.getStartsAt().toString().contains(searchText)).collect(Collectors.toList());
                break;

            default:
                break;
        }
        return tasks;
    }


    public List<VolunteerOpportunitiesDTO> getAllTaskForOrg(Organization organization) {
        return volunteerOppRepo.findAllByCreatedBy(organization).stream().map(task -> new VolunteerOpportunitiesDTO(task.getTaskId(), task.getTitle(), task.getDescription(), task.getLocation(), task.getSkills_required(), task.getOrganization_name(), task.getDeadLineForReg(), task.getStartsAt(), task.getEndsAt(), task.getStatus())).collect(Collectors.toList());
    }

    public VolunteerOpportunities createTask(VolunteerOpportunities volunteerOpportunities) {

        return volunteerOppRepo.save(volunteerOpportunities);

    }

    public List<VolunteerOpportunitiesDTO> searchVolOpp(String filterBy, String searchText) {
        return (switch (filterBy.toLowerCase()) {
            case "title" -> volunteerOppRepo.findByTitleContainingIgnoreCase(searchText);
            case "location" -> volunteerOppRepo.findByLocationContainingIgnoreCase(searchText);
            case "date" -> volunteerOppRepo.findByStartsAtLike(searchText);
            case "skills" -> volunteerOppRepo.findBySkills_Required(searchText);
            default -> volunteerOppRepo.findAll();
        }).stream().map(task -> new VolunteerOpportunitiesDTO(task.getTaskId(), task.getTitle(), task.getDescription(), task.getLocation(), task.getSkills_required(), task.getOrganization_name(), task.getDeadLineForReg(), task.getStartsAt(), task.getEndsAt(), task.getStatus())).collect(Collectors.toList());
    }


    public VolunteerOpportunities updateTask(VolunteerOpportunities volunteerOpportunities) {
        return volunteerOppRepo.save(volunteerOpportunities);
    }

    public Optional<VolunteerOpportunities> findById(Integer id) {
        return volunteerOppRepo.findById(id);
    }

    public void deleteTask(Integer id) {
        volunteerOppRepo.deleteById(id);
    }
}
