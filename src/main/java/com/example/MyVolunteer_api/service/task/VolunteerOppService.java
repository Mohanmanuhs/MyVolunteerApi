package com.example.MyVolunteer_api.service.task;

import com.example.MyVolunteer_api.model.task.VolunteerOpportunities;
import com.example.MyVolunteer_api.repository.task.VolunteerOppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerOppService {

    @Autowired
    private VolunteerOppRepo volunteerOppRepo;


    public List<VolunteerOpportunities> getAllTask(){
        return volunteerOppRepo.findAll();
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
