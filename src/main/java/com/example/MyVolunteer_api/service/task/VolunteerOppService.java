package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.dto.VolunteerOpportunitiesDTO;
import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
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
        return volunteerOppRepo.findAll()
                .stream().map(task -> new VolunteerOpportunitiesDTO(
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

    public VolunteerOpportunities createTask(VolunteerOpportunities volunteerOpportunities) {

        return volunteerOppRepo.save(volunteerOpportunities);

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
